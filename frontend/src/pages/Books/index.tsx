import BookHero from "@components/Book/BookHero";
import AuthGuard from "@components/Guards/AuthGuard";
import ReadingCard from "@components/Reading/ReadingCard";
import { Book } from "@models/book";
import { Reading } from "@models/reading";
import { fetchBook } from "@services/books";
import { fetchReadingsOfBook } from "@services/readings";
import { useLoading } from "@stores/useLoading";
import useUser from "@stores/useUser";
import { useCallback, useEffect, useMemo, useState } from "react";
import { FaTriangleExclamation } from "react-icons/fa6";
import { Link, useParams } from "react-router-dom";

export default function Books() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const { id } = useParams();
  const bookId = Number.parseFloat(id ?? "-1");
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);
  const user = useUser((state) => state.user);

  const [book, setBook] = useState<Book>();
  const [readings, setReadings] = useState<Reading[]>([]);

  const notFinishedReadings = useMemo(() => {
    return readings.filter((reading) => !reading.finished);
  }, [readings]);

  const countNotFinishedReadings = useMemo(() => {
    return notFinishedReadings.length;
  }, [notFinishedReadings.length]);

  const notFinishedReadingsIsFull = useMemo(() => {
    return (
      countNotFinishedReadings >= 5 ||
      (!user?.canHaveFiveReadings() && countNotFinishedReadings >= 2)
    );
  }, [countNotFinishedReadings, user]);

  const update = useCallback(() => {
    startLoading("book");
    startLoading("readings");
    fetchBook(bookId)
      .then((book) => setBook(book))
      .catch(() => setBook(undefined))
      .finally(() => stopLoading("book"));
    fetchReadingsOfBook(bookId)
      .then((readings) => setReadings(readings))
      .catch(() => setReadings([]))
      .finally(() => stopLoading("readings"));
  }, [startLoading, stopLoading, bookId]);

  useEffect(update, [update]);

  return (
    <main className="flex flex-col w-full h-ful items-center bg-base-100">
      <BookHero
        onUpdateClassification={update}
        book={book}
        reading={
          notFinishedReadings.length > 0 ? notFinishedReadings[0] : undefined
        }
        notFinishedReadingsIsFull={notFinishedReadingsIsFull}
      />
      <section className="flex flex-col gap-8 p-8 w-full">
        {notFinishedReadingsIsFull && (
          <div role="alert" className="alert alert-warning alert-soft">
            <FaTriangleExclamation className="size-6" />
            <span className="-ml-1">
              Limite de leituras simultâneas atingindo!
            </span>
            {countNotFinishedReadings < 5 && (
              <div>
                <Link to="/subscribe" className="btn btn-warning">
                  Aumentar limite
                </Link>
              </div>
            )}
          </div>
        )}
        <ul className="flex flex-col gap-8">
          {readings.map((reading) => (
            <ReadingCard onFinish={update} key={reading.id} reading={reading} />
          ))}
        </ul>
      </section>
    </main>
  );
}
