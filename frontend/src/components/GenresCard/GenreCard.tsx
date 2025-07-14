import Button from "@components/Button";
import { useState } from "react";
import { FaBookmark, FaRegHeart } from "react-icons/fa";
import { FaXmark } from "react-icons/fa6";

export interface Genre {
  id: string;
  name: string;
  color: string;
  icon?: string;
}

interface GenreCardProps {
  genre: Genre;
  onLike: (genre: Genre) => void;
  onDislike: (genre: Genre) => void;
  isAnimating?: boolean;
  bgColor?: string;
}

export function GenreCard({
  genre,
  onLike,
  onDislike,
  isAnimating = false,
  bgColor,
}: GenreCardProps) {
  const [swipeDirection, setSwipeDirection] = useState<"left" | "right" | null>(
    null
  );

  const handleLike = () => {
    setSwipeDirection("right");
    setTimeout(() => onLike(genre), 150);
  };

  const handleDislike = () => {
    setSwipeDirection("left");
    setTimeout(() => onDislike(genre), 150);
  };

  const color = bgColor || genre.color;

  return (
    <div
      className={`
        relative max-w-full w-80 h-[500px] p-6
        flex flex-col justify-between
        rounded-lg
        bg-[hsl(0 0% 100%)] text-[hsl(25 25% 15%)]
        bg-gradient-to-br from-[hsl(0 0% 100%)] to-[hsl(45 40% 95%)]
        border-2 border-[hsl(40 25% 85% / 0.5)]
        shadow-[0_4px_15px_-2px_hsl(25 25% 15% / 0.1)]
        transition-all duration-300 hover:shadow-2xl
        ${isAnimating ? "animate-float-in" : ""}
        ${swipeDirection === "left" ? "animate-swipe-left" : ""}
        ${swipeDirection === "right" ? "animate-swipe-right" : ""}
      `}
      style={{
        backgroundColor: `${color}15`,
        borderColor: `${color}50`,
      }}
    >
      <div className="text-center space-y-4">
        <div
          className="w-20 h-20 mx-auto rounded-full flex items-center justify-center text-3xl"
          style={{ backgroundColor: color + "20", color: color }}
        >
          <FaBookmark size={32} />
        </div>
        <h2 className={`text-2xl font-bold text-foreground tracking-wide transition-opacity duration-500 ${isAnimating ? "opacity-100" : "opacity-0"}`}>
          {genre.name}
        </h2>
      </div>
      <div className="flex justify-center gap-4 mt-6">
        <Button
          className="btn btn-circle btn-xl btn-secondary"
          onClick={handleDislike}
        >
          <FaXmark />
        </Button>
        <Button
          className="btn btn-circle btn-xl btn-primary"
          onClick={handleLike}
        >
          <FaRegHeart />
        </Button>
      </div>
    </div>
  );
}
