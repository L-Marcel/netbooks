import { RatingInput } from "@components/Input/RatingInput";
import BookTags from "./BookTags";
import { Book } from "@stores/useBook";

interface Props {
  book: Book;
}

export default function BookHero({ book }: Props) {
  return (
    <div className="hero justify-start max-h-screen min-h-96 relative bg-gradient-to-br from-base-100 via-base-200 to-base-300">
      <div
        className="absolute right-0 lg:left-0 top-0 h-full w-[75vw] lg:w-[40vw] z-0 bg-no-repeat bg-right opacity-20 lg:opacity-40 [mask-image:linear-gradient(to_right,transparent_0%,black)]  lg:[mask-image:linear-gradient(to_left,transparent_0%,black)] bg-cover"
        style={{ backgroundImage: `url(${book.banner})` }}
      />
      <div className="hero-content py-7 md:py-8 px-4 lg:px-8 flex-col lg:flex-row lg:gap-12">
        <img
          src={book.cover}
          className="max-w-sm rounded-lg shadow-2xl not-lg:hidden"
          height={280}
          width={200}
        />
        <article className="flex flex-col gap-6">
          <header className="flex flex-col gap-4 md:gap-2">
            <h1 className="text-3xl lg:text-5xl font-bold">{book.title}</h1>
            <BookTags tags={book.tags} />
            <RatingInput readonly rate={book.classification} />
          </header>
          <main>
            <p className="text-sm md:text-base">{book.description}</p>
          </main>
          <footer>
            <button type="button" className="btn btn-primary">
              Começar a ler
            </button>
          </footer>
        </article>
      </div>
    </div>
  );
}
