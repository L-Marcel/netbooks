import AuthGuard from "@components/Guards/AuthGuard";
import { fetchReading, fetchReadingContent } from "@services/readings";
import { useLoading } from "@stores/useLoading";
import { useCallback, useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Reading } from "@models/reading";
import { Document, Page as DocumentPage, pdfjs } from 'react-pdf';
import 'react-pdf/dist/Page/AnnotationLayer.css';
import 'react-pdf/dist/Page/TextLayer.css';
import Button from "@components/Button";

pdfjs.GlobalWorkerOptions.workerSrc = new URL(
  'pdfjs-dist/build/pdf.worker.min.mjs',
  import.meta.url,
).toString();

export default function Readings() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const { id } = useParams();
  const readingId = Number.parseFloat(id ?? "-1");
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const [reading, setReading] = useState<Reading>();
  const [file, setFile] = useState<File>();

  const page = reading?.currentPage ?? 1;
  const maxPage = reading?.numPages ?? 1;

  const updateContent = useCallback((page: number) => {
    startLoading("reading-content");
    fetchReadingContent(readingId, Math.max(page, 1))
      .then(setFile)
      .catch(() => setFile(undefined))
      .finally(() => stopLoading("reading-content"));
  }, [startLoading, stopLoading, readingId]);

  const update = useCallback(() => {
    startLoading("reading");
    fetchReading(readingId)
      .then(setReading)
      .catch(() => setReading(undefined))
      .finally(() => stopLoading("reading"));
  }, [startLoading, stopLoading, readingId]);

  useEffect(() => updateContent(page), [updateContent, page]);
  useEffect(update, [update]);

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

  return (
    <main className="flex flex-col w-full h-ful items-center bg-base-100">
      <section className="flex flex-row gap-4 px-4 h-18 items-center justify-between bg-base-300 w-full">
        <div className="flex flex-row gap-4 h-full items-center">
          <Link to={"/books/" + reading?.book} className="btn btn-md btn-primary">
            Voltar
          </Link>
          <p>{file?.name.replace(".pdf", "")}</p>
        </div>
        <div className="flex flex-row gap-4 h-full items-center">
          <Button onClick={onPreviousPage} disabled={page <= 1} className="btn btn-md btn-primary">
            Anterior
          </Button>
          <p>{page}/{maxPage}</p>
          <Button onClick={onNextPage} disabled={page >= maxPage} className="btn btn-md btn-primary">
            Próxima
          </Button>
        </div>
      </section>
      <section className="flex max-w-full overflow-auto max-h-[calc(100vh-4.5rem)] flex-col py-4 pl-4">
        <div className="flex w-min max-w-min max-h-full flex-col mr-4">
          <Document
            file={file}
            onLoadSuccess={() => {}}
          >
            <DocumentPage className="overflow-hidden rounded-box" pageNumber={1} />
          </Document>
        </div>
      </section>
    </main>
  );
}
