import AuthGuard from "@components/Guards/AuthGuard";
import { fetchReading, fetchReadingContent } from "@services/readings";
import { useLoading } from "@stores/useLoading";
import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Reading } from "@models/reading";
import { Document, Page as DocumentPage, pdfjs } from "react-pdf";
import "react-pdf/dist/Page/AnnotationLayer.css";
import "react-pdf/dist/Page/TextLayer.css";
import Button from "@components/Button";
import {
  motion,
  useMotionTemplate,
  useMotionValue,
  useMotionValueEvent,
  useTransform,
} from "motion/react";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import { debounce } from "es-toolkit";
import { ScaleSlider } from "@components/Input/BookScaleSlider";
import useElementSize from "../../hooks/useElementSize";
import { useGesture } from "react-use-gesture";

pdfjs.GlobalWorkerOptions.workerSrc = new URL(
  "pdfjs-dist/build/pdf.worker.min.mjs",
  import.meta.url
).toString();

export default function Readings() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Loading() {
  return <motion.div className="skeleton min-h-[1000px] min-w-[884px]" />;
}

function Page() {
  const { id } = useParams();
  const navigate = useNavigate();
  const abortControllerRef = useRef<AbortController | null>(null);
  const readingId = Number.parseFloat(id ?? "-1");
  const hasAnyLoading = useLoading((state) => state.hasAny);
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const constraintsRef = useRef({ top: 0, left: 0, bottom: 0, right: 0 });
  const { ref, size } = useElementSize();
  const [documentSize, setDocumentSize] = useState({
    width: 1,
    height: 1,
  });

  const targetScale = useMemo(() => {
    const scaleToFitWidth = size.width / documentSize.width;
    const scaleToFitHeight = size.height / documentSize.height;
    return Math.min(scaleToFitWidth, scaleToFitHeight);
  }, [size, documentSize]);

  const scale = useMotionValue(1);
  const x = useMotionValue(0);
  const y = useMotionValue(0);
  const min = useMotionValue(scale.get() * (1 / 6));
  const max = useMotionValue(2 * 6);
  const pageWidth = useMotionValue(size.width);
  const pageHeight = useMotionValue(size.height);
  const _pageWidth = useMotionTemplate`${pageWidth}px`;
  const _pageHeight = useMotionTemplate`${pageHeight}px`;

  const bind = useGesture(
    {
      onPinch: ({ offset: [s] }) => {
        scale.set(s);
      },
    },
    {
      pinch: {
        initial: () => [scale.get(), 0],
        distanceBounds: { min: min.get(), max: max.get() },
      },
    }
  );

  const [reading, setReading] = useState<Reading>();
  const [file, setFile] = useState<File>();

  const page = reading?.currentPage;
  const maxPage = reading?.numPages;

  const debouncedFetch = useMemo(
    () =>
      debounce((readingId: number, page: number) => {
        if (abortControllerRef.current) abortControllerRef.current.abort();

        const newController = new AbortController();
        abortControllerRef.current = newController;

        stopLoading("reading-content-request");
        startLoading("reading-content");
        fetchReadingContent(readingId, Math.max(page, 1), newController.signal)
          .then(setFile)
          .catch((err) => {
            if (err.name !== "AbortError") setFile(undefined);
          })
          .finally(() => {
            stopLoading("reading-content");
          });
      }, 500),
    [startLoading, stopLoading, setFile]
  );

  const updateContent = useCallback(
    (page?: number) => {
      if (readingId > 0 && page) {
        startLoading("reading-content-request");
        debouncedFetch(readingId, page);
      }
    },
    [debouncedFetch, startLoading, readingId]
  );

  const update = useCallback(() => {
    startLoading("reading");
    fetchReading(readingId)
      .then(setReading)
      .catch(() => setReading(undefined))
      .finally(() => stopLoading("reading"));
  }, [startLoading, stopLoading, readingId]);

  useEffect(() => {
    min.set(targetScale * (1 / 6));
    max.set(targetScale * 6);
    scale.set(targetScale);
  }, [scale, min, max, pageWidth, pageHeight, targetScale]);

  useMotionValueEvent(scale, "change", (latest) => {
    pageWidth.set(documentSize.width * latest);
    pageHeight.set(documentSize.height * latest);
    constraintsRef.current = {
      left: -size.width / 2 - (documentSize.width * latest) / 2 + 64,
      right: size.width / 2 + (documentSize.width * latest) / 2 - 64,
      top: -size.height / 2 - (documentSize.height * latest) / 2 + 64,
      bottom: size.height / 2 + (documentSize.height * latest) / 2 - 64,
    };
  });

  const clampedX = useTransform(x, (rawX) => {
    const { left, right } = constraintsRef.current;
    return Math.max(left, Math.min(right, rawX));
  });

  const clampedY = useTransform(y, (rawY) => {
    const { top, bottom } = constraintsRef.current;
    return Math.max(top, Math.min(bottom, rawY));
  });

  useEffect(() => updateContent(page), [updateContent, page]);
  useEffect(update, [update]);
  useEffect(() => {
    return () => {
      debouncedFetch.cancel();
    };
  }, [debouncedFetch]);

  const onPreviousPage = () => {
    startLoading("reading-content");
    setReading((reading) => {
      reading?.previousPage();
      return reading?.clone();
    });
  };

  const onNextPage = () => {
    startLoading("reading-content");
    setReading((reading) => {
      reading?.nextPage();
      return reading?.clone();
    });
  };

  const onSetPage = (page: number) => {
    startLoading("reading-content");
    setReading((reading) => {
      reading?.setPage(page);
      return reading?.clone();
    });
  };

  const onBack = () => {
    if (window.history.length > 1) {
      navigate(-1);
    } else {
      navigate("/books/" + reading?.book, { replace: true });
    }
  };

  const onWheel = useCallback(
    (e: React.WheelEvent) => {
      const currentScale = scale.get();
      const minScale = min.get();
      const maxScale = max.get();
      const zoomFactor = 0.001;
      const scaleChange = e.deltaY * -zoomFactor;

      let newScale = currentScale + scaleChange;

      newScale = Math.max(minScale, Math.min(maxScale, newScale));
      scale.set(newScale);
    },
    [scale, min, max]
  );

  return (
    <main className="flex flex-col w-full h-full min-h-[calc(100dvh-4rem)] items-center bg-base-100">
      <section className="grid touch-none grid-cols-2 md:grid-cols-3 overflow-hidden flex-row gap-4 px-4 min-h-18 max-h-18 items-center bg-base-300 w-full">
        <div className="flex flex-row gap-4 h-full items-center overflow-hidden w-full">
          <Button onClick={onBack} className="btn btn-md btn-primary">
            Voltar
          </Button>
          <p className="overflow-hidden not-sm:hidden max-w-11/12 text-nowrap overflow-ellipsis">
            {file?.name.replace(".pdf", "")}
          </p>
        </div>
        <progress
          className="not-md:hidden justify-self-center progress progress-primary w-56"
          value={page ?? 1}
          max={maxPage ?? 1}
        ></progress>
        <div className="flex grow justify-self-end flex-row gap-4 h-full items-center">
          <Button
            notDisableOnLoading
            onClick={onPreviousPage}
            disabled={!page || page <= 1}
            className="btn btn-md btn-primary btn-square"
          >
            <FaArrowLeft />
          </Button>
          <div className="flex not-sm:hidden flex-row gap-2 h-full w-min items-center">
            <input
              type="number"
              min={1}
              max={maxPage}
              className="input input-sm input-primary w-min"
              onChange={(e) =>
                onSetPage(Number.parseInt(e.currentTarget.value))
              }
              value={page ?? 1}
            />
            /{maxPage ?? 1}
          </div>
          <Button
            notDisableOnLoading
            onClick={onNextPage}
            disabled={!page || !maxPage || page >= maxPage}
            className="btn btn-md btn-primary btn-square"
          >
            <FaArrowRight />
          </Button>
        </div>
      </section>
      <section
        ref={ref}
        onWheelCapture={onWheel}
        {...bind()}
        className="relative p-2 lg:p-8 flex w-full max-w-full overflow-hidden h-[calc(100dvh-4.5rem)] flex-col"
      >
        <ScaleSlider
          onReset={() => {
            scale.set(targetScale);
            x.set(0);
            y.set(0);
          }}
          min={min}
          max={max}
          scale={scale}
        />
        <div className="flex -mt-12 mx-auto w-min max-w-full max-h-full flex-col">
          {file && !hasAnyLoading ? (
            <Document
              onLoadSuccess={async (document) => {
                const page = await document.getPage(1);
                const { width, height } = page.getViewport({ scale: 1 });
                setDocumentSize({ width, height });
              }}
              file={file}
              noData={<Loading />}
              loading={<Loading />}
            >
              <motion.div
                drag
                dragMomentum={false}
                _dragX={x}
                _dragY={y}
                style={{
                  width: _pageWidth,
                  height: _pageHeight,
                  x: clampedX,
                  y: clampedY,
                }}
                initial={{ opacity: 0 }}
                animate={{ opacity: 1, animationDuration: 1 }}
              >
                <DocumentPage
                  _className="!w-full !h-full "
                  className="overflow-hidden rounded-box max-w-full !min-h-min !min-w-min"
                  pageNumber={1}
                />
              </motion.div>
            </Document>
          ) : (
            <Loading />
          )}
        </div>
      </section>
    </main>
  );
}
