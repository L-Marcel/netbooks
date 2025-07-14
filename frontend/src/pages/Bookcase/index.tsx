import BookCover from "@components/Book/BookCover";
import AuthGuard from "@components/Guards/AuthGuard";
import { searchBooksFromBookcase } from "@services/books";
import { useEffect, useMemo, useState } from "react";
import { FaSearch } from "react-icons/fa";
import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { debounce } from "es-toolkit";

export default function Bookcase() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

const STALE_TIME = 1000 * 60;

function Page() {
  const [query, setQuery] = useState<string>("");
  const [input, setInput] = useState<string>("");

  const debouncedQuery = useMemo(
    () => debounce((query: string) => setQuery(query), 750),
    [setQuery]
  );

  useEffect(() => {
    debouncedQuery(input);
  }, [debouncedQuery, input]);

  const { data: books = [], isFetching } = useQuery({
    queryKey: ["books", "bookcase", query],
    queryFn: async ({ queryKey }) => await searchBooksFromBookcase(queryKey[1]),
    retry: 3,
    staleTime: STALE_TIME,
    refetchOnMount: true,
  });

  return (
    <main className="flex p-8 flex-col w-full h-full min-h-[calc(100dvh-4rem)] items-center bg-base-100">
      <section className="flex flex-col w-full gap-2 bg-base-200 p-6 rounded-box">
        <div className="flex flex-col">
          <h3 className="text-xl font-semibold">Buscar livro da estante</h3>
          <p>Por título, descrição, editora, autores ou marcadores</p>
        </div>
        <label className="input focus-within:input-primary">
          <FaSearch className="" />
          <input
            value={input}
            onChange={(e) => setInput(e.target.value)}
            placeholder="Pesquisa"
            type="text"
          />
        </label>
      </section>
      {!isFetching && books.length <= 0 && (
        <p className="mt-4 justify-self-start w-full pl-2 text-start text-lg font-semibold">
          Você não iniciou nenhuma leitura!
        </p>
      )}
      <section className="mt-4 flex flex-wrap gap-4 max-w-full w-full">
        {isFetching &&
          [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12].map((value, index) => {
            return <BookCover key={value} index={index} />;
          })}
        {!isFetching &&
          books.map((book, index) => {
            return (
              <Link
                key={book.id}
                to={"/books/" + book.id}
                className="border-none relative outline-none rounded-xl group block hover:opacity-60 focus-visible:opacity-95 min-w-[100px] max-w-[100px] max-h-[150px] min-h-[150px] lg:min-w-[200px] lg:max-w-[200px] lg:max-h-[300px] lg:min-h-[300px]"
              >
                <div
                  className={`z-10 rounded-lg overflow-hidden flex flex-row absolute inset-0 group-focus-visible:border-3 ${book.isPremium() ? "group-focus-visible:border-warning" : "group-focus-visible:border-primary"}`}
                />
                <BookCover
                  id={book.id}
                  index={index}
                  premium={book.isPremium()}
                  cover={book.getCoverUrl()}
                />
              </Link>
            );
          })}
      </section>
    </main>
  );
}
