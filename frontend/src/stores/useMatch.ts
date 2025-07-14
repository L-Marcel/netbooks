import { create } from "zustand";

export type Genres = string[];

type MatchStorage = {
    room: string;
    genres?: Genres;
    selectedOptions: Map<string, number>;
    setRoom: (room: string) => void;
    setGenres: (genres: Genres) => void;
    setSelectedOptions: (options: Map<string, number>) => void;
    likeGenre: (genre: string) => void;
    dislikeGenre: (genre: string) => void;
    getTop3Genres: () => string[];
};

const useMatch = create<MatchStorage>((set, get) => ({
    room: "",
    genres: undefined,
    selectedOptions: new Map<string, number>(),

    setRoom: (room: string) => set({ room }),

    setGenres: (genres: Genres) => set({ genres }),

    setSelectedOptions: (options: Map<string, number>) =>
        set({ selectedOptions: options }),

    likeGenre: (genre: string) => {
        const currentMap = new Map(get().selectedOptions);
        const currentValue = currentMap.get(genre) ?? 0;
        currentMap.set(genre, currentValue + 1);
        set({ selectedOptions: currentMap });
    },

    dislikeGenre: (genre: string) => {
        const currentMap = new Map(get().selectedOptions);
        const currentValue = currentMap.get(genre) ?? 0;
        currentMap.set(genre, currentValue - 1);
        set({ selectedOptions: currentMap });
    },

    getTop3Genres: () => {
        const entries = Array.from(get().selectedOptions.entries());
        entries.sort((a, b) => b[1] - a[1]);
        return entries.slice(0, 3).map(([genre]) => genre);
    },
}));

export default useMatch;
