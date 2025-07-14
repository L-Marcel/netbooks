import BookForm from "@components/Book/BookForm";
import AdminGuard from "@components/Guards/AdminGuard";
import NotFound from "@components/NotFound";
import { Book } from "@models/book";
import { fetchBook } from "@services/books";
import { useLoading } from "@stores/useLoading";
import { useCallback, useEffect, useState } from "react";
import { useParams } from "react-router-dom";

export default function EditBook() {
  return (
    <AdminGuard>
      <Page />
    </AdminGuard>
  );
}

export function Page() {
  const { id } = useParams();
  const bookId = Number.parseFloat(id ?? "-1");
  const [book, setBook] = useState<Book>();

  const loadingSet = useLoading((state) => state.loadingSet);
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const update = useCallback(() => {
    startLoading("book");
    fetchBook(bookId, true)
      .then((book) => setBook(book))
      .catch(() => setBook(undefined))
      .finally(() => stopLoading("book"));
  }, [startLoading, stopLoading, bookId]);

  useEffect(update, [update]);

  if (!loadingSet.has("book") && !book) return <NotFound />;

  return (
    <main className="flex flex-col p-8 gap-4 bg-base-100 w-full h-full min-h-[calc(100dvh-4rem)] items-center">
      <section className="flex flex-col gap-8 w-full"></section>
      {book && <BookForm book={book} />}
    </main>
  );
}
