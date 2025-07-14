import BookCarousel from "@components/Book/BookCarousel";
import BookHero from "@components/Book/BookHero";
import { Book } from "@models/book";
import { fetchBooks } from "@services/books";
import { fetchTags } from "@services/tags";
import { useQuery } from "@tanstack/react-query";

export default function Home() {
  const { data: books = [], refetch } = useQuery({
    queryKey: ["books"],
    queryFn: fetchBooks,
    retry: 3,
  });

  const { data: tags = [] } = useQuery({
    queryKey: ["tags"],
    queryFn: fetchTags,
    retry: 3,
  });

  const initialTagsRecord = tags.reduce(
    (prev, curr) => {
      prev[curr.name] = [];
      return prev;
    },
    {} as Record<string, Book[]>
  );

  const mapOfBooks = books.reduce(
    (prev, curr) => {
      if (!curr) return prev;

      for (const tag of curr.tags ?? []) {
        if (prev[tag.name]) prev[tag.name].push(curr);
        else prev[tag.name] = [];
      }

      return prev;
    },
    initialTagsRecord as Record<string, Book[]>
  );

  return (
    <main className="flex flex-col w-full h-full min-h-[calc(100dvh-4rem)] items-center bg-base-100">
      <BookHero
        onUpdateClassification={refetch}
        book={books.length > 0 ? books[0] : undefined}
      />
      {Object.entries(mapOfBooks)
        .filter(([, books]) => books.length >= 4)
        .sort(([, a], [, b]) => b.length - a.length)
        .map(([tag, books]) => (
          <BookCarousel
            key={tag}
            tag={{
              name: tag,
              score: 0,
            }}
            books={books}
          />
        ))}
    </main>
  );
}
