import BookCarousel from "@components/Book/BookCarousel";
import BookHero from "@components/Book/BookHero";
import Navbar from "@components/Navbar";
import { fetchBooks } from "@services/books";
import { useQuery } from "react-query";

export default function Home() {
  const { data: books = [] } = useQuery({
    queryKey: ['books'],
    queryFn: () => fetchBooks(),
  })

  return (
    <>
      <header>
        <Navbar />
      </header>
      <main className="flex flex-col w-full h-ful min-h-screen items-center bg-base-100">
        <div className="carousel w-full h-full">
          <div className="carousel-item w-full h-full">
            <BookHero book={books.length > 0? books[0]:undefined} />
          </div>
        </div>
        <BookCarousel books={books} />
        <BookCarousel books={books} />
        <BookCarousel books={books} />
        <BookCarousel books={books} />
      </main>
    </>
  );
}
