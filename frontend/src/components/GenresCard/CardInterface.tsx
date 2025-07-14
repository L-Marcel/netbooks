import { useState } from "react";
import { Genre, GenreCard } from "./GenreCard";
import useMatch from "@stores/useMatch";
import useRoom from "@stores/useRoom";
import Loader from "@components/SimpleLoading";

function randomColor(): string {
  const lettersHex = '0123456789ABCDEF';
  let cor = '#';
  for (let i = 0; i < 6; i++) {
    cor += lettersHex[Math.floor(Math.random() * 16)];
  }
  return cor;
}

type CardsProps = {
  genresOptions: string[];
};

export default function Cards({ genresOptions }: CardsProps) {
  const genres: Genre[] = genresOptions.map((genre, index) => ({
    id: String(index),
    name: genre,
    color: randomColor(),
  }));

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
        setCurrentGenreIndex(prev => prev + 1);
        setIsAnimating(true);
      } else {
        setCurrentGenreIndex(genres.length);
        useRoom.getState().setVoted();
      }
    }, 300);
  };

  return (
    <div className="relative">
      {currentGenreIndex < genres.length && currentGenre && (
        <div
          className="flex justify-center items-center transition-colors duration-300"
          style={{
            backgroundColor: bgColor ? `${bgColor}20` : 'transparent'
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
      )}
      {currentGenreIndex >= genres.length && (
        <div className="block text-center text-lg mt-4">
          <Loader text="Aguardando demais Participantes..." />
        </div>
      )}
    </div>
  );
}

