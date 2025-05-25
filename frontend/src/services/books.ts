import api from "./axios";

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

function putBookLinks(book: Book): Book {
  return {
    ...book,
    cover: `http://localhost:8080/books/covers/${book.id}.webp`,
    banner: `http://localhost:8080/books/banners/${book.id}.webp`,
  };
}

export async function fetchBooks(): Promise<Book[]> {
  return api
    .get<Book[]>("books")
    .then((response) =>
      response.data.map(putBookLinks).sort((a, b) => b.stars - a.stars)
    );
}

export async function fetchTags(): Promise<Tag[]> {
  return api.get<Tag[]>("tags").then((response) => response.data);
}
