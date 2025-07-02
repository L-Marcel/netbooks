import { RatingInput } from "@components/Input/RatingInput";
import BookTags from "./BookTags";
import { Book } from "@models/book";
import { Link, useNavigate } from "react-router-dom";
import Button from "@components/Button";
import { Reading } from "@models/reading";
import { fetchReadingByBook, startReadingsOfBook } from "@services/readings";
import { useLoading } from "@stores/useLoading";
import Loading from "@components/Loading";
import { useEffect, useState } from "react";
import { FaBookBookmark } from "react-icons/fa6";
import useUser from "@stores/useUser";
interface Props {
  book?: Book;
  reading?: Reading;
  notFinishedReadingsIsFull?: boolean;
}

export default function BookHero({
  book,
  reading,
  notFinishedReadingsIsFull = false,
}: Props) {
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

  useEffect(() => {
    if (reading) setLastReading(reading);
    else {
      startLoading("book-hero");
      fetchReadingByBook(book?.id ?? -1)
        .then((reading) => setLastReading(reading))
        .catch(() => setLastReading(reading))
        .finally(() => stopLoading("book-hero"));
    }
  }, [reading, setLastReading, book?.id, startLoading, stopLoading]);

  return (
    <section className="hero justify-start max-h-screen min-h-96 relative bg-gradient-to-br from-base-100 via-base-200 to-base-300 border-b-8 border-base-200">
      <div
        className="absolute right-0 lg:left-0 top-0 h-full w-[75vw] lg:w-[40vw] z-0 bg-no-repeat bg-right opacity-20 lg:opacity-40 [mask-image:linear-gradient(to_right,transparent_0%,black)]  lg:[mask-image:linear-gradient(to_left,transparent_0%,black)] bg-cover"
        style={{ backgroundImage: `url(${book?.banner})` }}
      />
      <div className="hero-content items-start py-7 md:py-8 px-4 lg:px-8 flex-col lg:flex-row lg:gap-12">
        <img
          src={book?.cover}
          className="max-w-sm rounded-lg shadow-2xl not-lg:hidden"
          height={280}
          width={200}
        />
        <article className="flex flex-col gap-6 pt-8">
          <header className="flex flex-col gap-4 md:gap-2">
            <h1 className="text-3xl lg:text-5xl font-bold">{book?.title}</h1>
            <BookTags tags={book?.tags ?? []} />
            <RatingInput readonly rate={book?.stars ?? 0} />
          </header>
          <main>
            <p className="text-sm md:text-base">{book?.description}</p>
          </main>
          <footer className="flex flex-row items-center gap-4">
            <Loading
              id="book-hero"
              loadingMessage="Carregando..."
              defaultMessage={
                <>
                  {!reading && user && (
                    <Link
                      to={"/books/" + book?.id}
                      className="btn btn-square btn-secondary"
                    >
                      <FaBookBookmark />
                    </Link>
                  )}
                  {lastReading && user && (
                    <Link
                      to={"/readings/" + lastReading?.id}
                      className={`btn ${!reading ? "btn-primary" : "btn-secondary"}`}
                    >
                      Continuar leitura
                    </Link>
                  )}
                  {user? (!!reading && (
                    <Button
                      disabled={notFinishedReadingsIsFull}
                      onClick={onNewReadRequested}
                      className="btn btn-primary"
                    >
                      <Loading
                        id={loadingId}
                        loadingMessage="Iniciando leitura..."
                        defaultMessage="Nova leitura"
                      />
                    </Button>
                  )):(
                    <Link
                      to="/subscribe"
                      className="btn btn-primary"
                    >
                      Nova leitura
                    </Link>
                  )}
                </>
              }
            />
          </footer>
        </article>
      </div>
    </section>
  );
}
