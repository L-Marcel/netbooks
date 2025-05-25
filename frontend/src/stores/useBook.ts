import { create } from "zustand";

export type Author = {
  id: number;
  name: string;
};

export type Book = {
  id: number;
  isbn?: number;
  title: string;
  numPages: number;
  publishedIn: Date;
  publisher: string;
  description: string;
  stars: number;
  tags: string[];
  authors: Author[];
  cover: string;
  banner: string;
};

export type BookStore = {
  books: Book[];
};

const useBook = create<BookStore>(() => ({
  books: [],
}));

export default useBook;
