import BookCarousel from "@components/Book/BookCarousel";
import BookHero from "@components/Book/BookHero";
import { Book } from "@models/book";
import { fetchBooks, fetchTags } from "@services/books";
import { useQuery } from "react-query";

export default function Home() {
  const { data: books = [], refetch } = useQuery({
    queryKey: ["books"],
    queryFn: () => fetchBooks(),
  });

  const { data: tags = [] } = useQuery({
    queryKey: ["tags"],
    queryFn: () => fetchTags(),
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
      for (const tag of curr.tags) {
        prev[tag.name].push(curr);
      }

      return prev;
    },
    initialTagsRecord as Record<string, Book[]>
  );

  return (
    <main className="flex flex-col w-full h-ful items-center bg-base-100">
      <BookHero
        onUpdateClassification={refetch}
        book={books.length > 0 ? books[0] : undefined}
      />
      {Object.entries(mapOfBooks)
        .filter(([, books]) => books.length > 0)
        .sort(([, a], [, b]) => b.length - a.length)
        .map(([tag, books]) => (
          <BookCarousel
            key={tag}
            tag={{
              name: tag,
            }}
            books={books}
          />
        ))}
    </main>
  );
}
