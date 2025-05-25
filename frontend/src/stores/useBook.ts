import { create } from "zustand";

export type Author = {
  id: number;
  name: string;
};

export type Tag = {
  name: string;
};

export type Publisher = {
  name: string;
};

export type Book = {
  id: number;
  isbn?: number;
  title: string;
  numPages: number;
  publishedIn: Date;
  publisher: Publisher;
  description: string;
  stars: number;
  tags: Tag[];
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
