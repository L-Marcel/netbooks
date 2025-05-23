import BookCarousel from "@components/Book/BookCarousel";
import BookHero from "@components/Book/BookHero";
import Navbar from "@components/Navbar";
import { getMockedBooks } from "@services/mocks";

export default function Home() {
  const mockedBooks = getMockedBooks();

  return (
    <>
      <header>
        <Navbar />
      </header>
      <main className="flex flex-col w-full h-ful min-h-screen items-center bg-base-100">
        <div className="carousel w-full h-full">
          <div className="carousel-item w-full h-full">
            <BookHero book={mockedBooks[0]} />
          </div>
        </div>
        <BookCarousel books={mockedBooks} />
        <BookCarousel books={mockedBooks} />
        <BookCarousel books={mockedBooks} />
        <BookCarousel books={mockedBooks} />
      </main>
    </>
  );
}
