import { RatingInput } from "@components/Input/RatingInput";
import BookTags from "./BookTags";
import { Book } from "@models/book";
import { Link, useLocation, useNavigate } from "react-router-dom";
import Button from "@components/Button";
import { Reading } from "@models/reading";
import { fetchReadingByBook, startReadingsOfBook } from "@services/readings";
import { useLoading } from "@stores/useLoading";
import Loading from "@components/Loading";
import { useEffect, useState } from "react";
import { FaBookBookmark } from "react-icons/fa6";
import useUser from "@stores/useUser";
import BookClassificationDialog from "./BookClassificationDialog";
import { FaDownload } from "react-icons/fa";
import { BookRegisterData, downloadBook } from "@services/books";
import { motion } from "motion/react";
import { format } from "date-fns";
import { Benefit } from "@models/benefit";
import BookCover from "./BookCover";

interface Props {
  preview?: boolean;
  previewData?: BookRegisterData;
  book?: Book;
  reading?: Reading;
  onUpdateClassification?: () => void;
  notFinishedReadingsIsFull?: boolean;
}

const DEFAULT_ON_UPDATE_CLASSIFICATION = () => {};

export default function BookHero({
  book,
  reading,
  previewData,
  preview = false,
  notFinishedReadingsIsFull = false,
  onUpdateClassification = DEFAULT_ON_UPDATE_CLASSIFICATION,
}: Props) {
  const location = useLocation();
  const cover = previewData?.cover?.url ?? book?.getCoverUrl();
  const title = previewData?.title ?? book?.title;
  const description = previewData?.description ?? book?.description;
  const authors = previewData?.authors ?? book?.authors ?? [];
  const formattedAuthors = [
    ...authors.slice(0, authors.length - 2).map((author) => author.name),
    authors
      .slice(-2)
      .map((author) => author.name)
      .join(" e "),
  ].join(", ");

  const banner = previewData?.banner?.url ?? book?.getBannerUrl();
  const publisher = previewData?.publisher ?? book?.publisher;
  const publishedIn = previewData?.publishedIn ?? book?.publishedIn;
  const isPremium = (
    previewData?.requirements ??
    book?.requirements ??
    []
  ).includes(Benefit.CAN_READ_ALL_BOOKS);

  const navigate = useNavigate();
  const user = useUser((state) => state.user);
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);
  const loadingId = "new-reading-" + (book?.id ?? -1);
  const [lastReading, setLastReading] = useState<Reading>();

  const onNewReadRequested = () => {
    startLoading(loadingId);
    startReadingsOfBook(book?.id ?? -1)
      .then((reading) => navigate("/readings/" + reading?.id))
      .finally(() => stopLoading(loadingId));
  };

  const onDownloadBook = () => {
    downloadBook(book?.id ?? -1).then((file) => {
      const url = URL.createObjectURL(file);
      const link = document.createElement("a");
      link.href = url;
      link.download = file.name;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
    });
  };

  useEffect(() => {
    if (!preview && reading) setLastReading(reading);
    else if (!preview) {
      startLoading("book-hero");
      fetchReadingByBook(book?.id ?? -1)
        .then((reading) => setLastReading(reading))
        .catch(() => setLastReading(reading))
        .finally(() => stopLoading("book-hero"));
    }
  }, [preview, reading, setLastReading, book?.id, startLoading, stopLoading]);

  return (
    <section
      className={`group place-items-center flex flex-row w-full justify-start max-h-screen min-h-96 relative bg-gradient-to-br from-base-100 via-base-200 to-base-300 border-b-8 border-base-200 ${isPremium ? "is-premium border-warning-content" : ""}`}
    >
      <motion.div
        key={banner}
        initial={{ opacity: 0 }}
        animate={{ opacity: 0.4 }}
        transition={{ duration: 1 }}
        style={{
          backgroundImage: `url(${banner})`,
        }}
        className="absolute right-0 z-0 lg:left-0 top-0 h-full w-[75vw] lg:w-[40vw] bg-no-repeat bg-right opacity-20 lg:opacity-40 [mask-image:linear-gradient(to_right,transparent_0%,black)]  lg:[mask-image:linear-gradient(to_left,transparent_0%,black)] bg-cover"
      />
      <div className="flex w-full z-10 items-start py-7 md:py-8 px-4 lg:px-8 flex-col lg:flex-row lg:gap-10">
        <div className="relative max-w-sm min-w-[200px] max-h-[300px] not-lg:hidden">
          <BookCover
            withoutAdminButtons={preview}
            id={book?.id}
            premium={isPremium}
            cover={cover}
          />
          {publisher ? (
            <span className="badge absolute left-0 right-0 badge-ghost mx-auto font-extralight mt-2">
              {publisher?.name}
            </span>
          ) : (
            <div className="badge absolute left-0 right-0 mx-auto font-extralight mt-2 border-none skeleton min-w-26" />
          )}
        </div>
        <motion.article
          layout="size"
          transition={{
            layout: {
              duration: 0.1,
              type: "spring",
              bounce: 0,
              damping: 40,
              stiffness: 300,
            },
          }}
          className="flex flex-col not-xl:w-full grow gap-2.5 my-auto"
        >
          <header className="flex flex-col gap-4 md:gap-2">
            {publishedIn ? (
              <p className="font-extralight -mb-2 text-sm">
                {format(publishedIn, "dd/MM/yyyy")}
              </p>
            ) : (
              <p className="font-extralight -mb-2 text-sm skeleton" />
            )}
            {title ? (
              <h1 className="text-3xl font-bold w-[calc(100%-5rem)]">
                {title}
              </h1>
            ) : (
              <h1 className="skeleton h-9 w-[calc(100%-5rem)]" />
            )}
            <BookTags tags={previewData?.tags ?? book?.tags ?? []} />
            <div className="flex flex-row items-center gap-2">
              <RatingInput readonly rate={book?.stars ?? 8} />
              {user && !preview && book && (
                <BookClassificationDialog
                  onUpdate={onUpdateClassification}
                  book={book}
                />
              )}
              {user && !preview && book && user.canDownloadBook(book) && (
                <Button
                  onClick={onDownloadBook}
                  className="btn btn-ghost btn-primary group-[.is-premium]:btn-warning btn-sm"
                >
                  <FaDownload /> Baixar
                </Button>
              )}
            </div>
          </header>
          <main>
            {authors ? (
              <span className="text-sm font-thin italic max-w-md">
                {formattedAuthors}
              </span>
            ) : (
              <div className="skeleton h-5 max-w-sm" />
            )}
            {description ? (
              <p className="text-sm md:text-base max-w-md">{description}</p>
            ) : (
              <div className="flex flex-col gap-2 w-full">
                <div className="skeleton h-5 md:h-6 max-w-md" />
                <div className="skeleton h-5 md:h-6 max-w-md" />
                <div className="skeleton h-5 md:h-6 max-w-md" />
              </div>
            )}
          </main>
          {!preview && (
            <Loading
              id="book-hero"
              withoutSpinner
              defaultMessage={
                <motion.footer
                  initial={{
                    opacity: 0,
                  }}
                  animate={{
                    opacity: 1,
                  }}
                  transition={{
                    type: "tween",
                    duration: 0.5,
                  }}
                  className="flex flex-row items-center gap-2.5"
                >
                  {!reading &&
                    location.pathname !== "/books/" + book?.id &&
                    user && (
                      <Link
                        to={"/books/" + book?.id}
                        className="btn btn-sm btn-square btn-secondary"
                      >
                        <FaBookBookmark />
                      </Link>
                    )}
                  {lastReading && !lastReading.finished && user && (
                    <Link
                      to={"/readings/" + lastReading?.id}
                      className={`btn btn-sm ${!reading ? "btn-primary group-[.is-premium]:btn-warning" : "btn-secondary"}`}
                    >
                      Continuar leitura
                    </Link>
                  )}
                  {user ? (
                    <Button
                      disabled={notFinishedReadingsIsFull}
                      onClick={onNewReadRequested}
                      className="btn btn-sm btn-primary group-[.is-premium]:btn-warning"
                    >
                      <Loading
                        id={loadingId}
                        loadingMessage="Iniciando leitura..."
                        defaultMessage="Nova leitura"
                      />
                    </Button>
                  ) : (
                    <Link
                      to="/subscribe"
                      className="btn btn-sm btn-primary group-[.is-premium]:btn-warning"
                    >
                      Nova leitura
                    </Link>
                  )}
                </motion.footer>
              }
            />
          )}
        </motion.article>
      </div>
    </section>
  );
}
