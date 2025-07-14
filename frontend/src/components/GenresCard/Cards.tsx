import { useState } from "react";
import { Genre, GenreCard } from "./GenreCard";
import useMatch from "@stores/useMatch";
import Loader from "@components/SimpleLoading";
import BookTags from "@components/Book/BookTags";
import Button from "@components/Button";
import { sendSelectedGenres } from "@services/room";
import useRoom from "@stores/useRoom";

function randomColor(): string {
  const lettersHex = "0123456789ABCDEF";
  let cor = "#";

  for (let i = 0; i < 6; i++) {
    cor += lettersHex[Math.floor(Math.random() * 16)];
  };

  return cor;
};

type CardsProps = {
  genresOptions: string[];
};

export default function Cards({ genresOptions }: CardsProps) {
  const room = useRoom((state) => state.room);
  const genres: Genre[] = genresOptions.map((genre, index) => ({
    id: String(index),
    name: genre,
    color: randomColor(),
  }));

  const currentResult = useMatch((state) => state.currentResult);
  const results = useMatch((state) => state.results);

  const [currentGenreIndex, setCurrentGenreIndex] = useState(0);
  const [isAnimating, setIsAnimating] = useState(true);
  const { likeGenre, dislikeGenre } = useMatch.getState();
  const [bgColor, setBgColor] = useState<string | null>(null);

  const currentGenre = genres[currentGenreIndex];

  const handleLike = (genre: Genre) => {
    setIsAnimating(false);
    setBgColor(genre.color);
    likeGenre(genre.name);
    setTimeout(() => {
      setBgColor(null);
      moveToNext();
    }, 300);
  };

  const handleDislike = (genre: Genre) => {
    setIsAnimating(false);
    setBgColor(genre.color);
    dislikeGenre(genre.name);
    setTimeout(() => {
      setBgColor(null);
      moveToNext();
    }, 300);
  };

  const moveToNext = () => {
    setTimeout(() => {
      if (currentGenreIndex < genres.length - 1) {
        setCurrentGenreIndex((prev) => prev + 1);
        setIsAnimating(true);
      } else {
        setCurrentGenreIndex(genres.length);
        sendSelectedGenres(genres.map((genre) => genre.name), room?.code ?? "");
      }
    }, 300);
  };

  return (
    <div className="relative max-w-full">
      {currentResult ? (
        <div className="flex-col justify-center items-center mt-8 text-center">
          <div className="flex justify-center">
            <img
              src={currentResult.getCoverUrl()}
              className="object-cover shadow-2xl rounded-lg group-focus-visible:rounded-2xl w-[100px] h-[160px] md:w-[200px] md:h-[320px]"
              height={320}
              width={200}
            />
          </div>
          <div className="flex justify-center pt-3">
            <BookTags tags={currentResult.tags ?? []} />
          </div>
          <h1 className="text-2xl md:text-4xl font-semibold text-gray-800 dark:text-white pt-3 pb-3">
            {currentResult.title}
          </h1>
          <p className="text-sm md:text-base max-w-md">
            {currentResult.description}
          </p>

          {results && results.length > 1 && (
            <div className="mt-4 flex justify-center gap-2 flex-wrap">
              <Button
                onClick={() => {
                  const currentIndex = results.findIndex(
                    (r) => r === currentResult
                  );
                  const nextIndex = (currentIndex + 1) % results.length;
                  useMatch.getState().setCurrentResult(results[nextIndex]);
                }}
                className="btn btn-sm btn-outline"
              >
                Ver outra sugestão
              </Button>
            </div>
          )}
        </div>
      ) : currentGenreIndex < genres.length && currentGenre ? (
        <div
          className="flex justify-center items-center transition-colors duration-300"
          style={{
            backgroundColor: bgColor ? `${bgColor}20` : "transparent",
          }}
        >
          <GenreCard
            genre={currentGenre}
            onLike={handleLike}
            onDislike={handleDislike}
            isAnimating={isAnimating}
            bgColor={bgColor || currentGenre.color}
          />
        </div>
      ) : (
        <div className="block text-center text-lg mt-4">
          <Loader text="Aguardando..." />
        </div>
      )}
    </div>
  );
}
