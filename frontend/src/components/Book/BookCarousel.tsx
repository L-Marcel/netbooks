import Button from "@components/Button";
import useMediaQuery from "../../hooks/useMediaQuery";
import { remToPx } from "../../Utils/pixels";
import { Book } from "@models/book";
import { KeyboardEvent, useLayoutEffect, useRef, useState } from "react";
import { FaArrowLeft, FaArrowRight } from "react-icons/fa";
import { Link } from "react-router-dom";
import { Tag } from "@models/tag";
import BookCover from "./BookCover";

interface Props {
  tag: Tag;
  books: Book[];
}

export default function BookCarousel({ tag, books }: Props) {
  const carousel = useRef<HTMLDivElement>(null);
  const isLarge = useMediaQuery("(width >= 64rem)");
  const [viewportWidth, setViewportWidth] = useState(0);

  const ITEM_WIDTH = isLarge ? 200 : 100;

  const gap = remToPx(isLarge ? 2 : 1);
  const finalItemWidth = ITEM_WIDTH + gap;

  const visibleCount = Math.ceil(viewportWidth / finalItemWidth);
  const completeVisibleCount = Math.floor(viewportWidth / finalItemWidth);
  const needArrows = completeVisibleCount < books.length;

  useLayoutEffect(() => {
    function updateWidth() {
      if (carousel.current) {
        setViewportWidth(carousel.current.clientWidth);
      }
    }

    updateWidth();

    window.addEventListener("resize", updateWidth);
    return () => window.removeEventListener("resize", updateWidth);
  }, [carousel]);

  const onToNext = () =>
    carousel.current?.scrollBy({
      left: (ITEM_WIDTH + gap) * (visibleCount - 1),
      behavior: "smooth",
    });

  const onToPrevious = () =>
    carousel.current?.scrollBy({
      left: (ITEM_WIDTH + gap) * -(visibleCount - 1),
      behavior: "smooth",
    });

  const onKeyDown = (e: KeyboardEvent<HTMLDivElement>) => {
    if (e.key === "ArrowLeft") onToPrevious();
    else if (e.key === "ArrowRight") onToNext();
  };

  return (
    <section className="relative flex flex-col w-full overflow-hidden">
      <h4 className="text-xl lg:text-2xl px-4 lg:px-8 pt-2 lg:pt-4 text-base-content align-middle">
        {tag.name}{" "}
        <span className="font-light text-base">({books.length})</span>
      </h4>
      <hr className="mx-4 lg:mx-8 w-1/6 mt-2" />
      <div className="relative flex flex-col">
        {needArrows && (
          <div className="absolute z-10 left-6 right-6 lg:left-4 lg:right-4 top-1/2 flex -translate-y-[1.5rem] transform justify-between">
            <Button
              onClick={onToPrevious}
              className="btn btn-circle btn-soft btn-primary"
            >
              <FaArrowLeft />
            </Button>
            <Button
              onClick={onToNext}
              className="btn btn-circle btn-soft btn-primary"
            >
              <FaArrowRight />
            </Button>
          </div>
        )}
        <div
          ref={carousel}
          tabIndex={0}
          onKeyDown={onKeyDown}
          className="focus:outline-none focus:ring-2 ring-offset-2 ring-primary ring-offset-base-100 overflow-x-hidden flex flex-row scroll-smooth gap-4 lg:gap-8 max-h-[150px] min-h-[150px] lg:max-h-[300px] lg:min-h-[300px] mx-4 lg:mx-8 rounded-box mt-2 lg:mt-4 mb-2 lg:mb-4"
          style={{
            width: `calc(100% - ${gap * 2}px)`,
          }}
        >
          {books.map((book) => {
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
                  premium={book.isPremium()}
                  cover={book.getCoverUrl()}
                />
              </Link>
            );
          })}
        </div>
      </div>
    </section>
  );
}
