import { Book, BookData } from "@models/book";
import api from "./axios";
import { ImageData } from "./image";
import { Benefit } from "@models/benefit";
import { Author } from "@models/author";
import { Publisher } from "@models/publisher";
import { Tag } from "@models/tag";

export type BookRegisterData = {
  isbn?: number;
  title: string;
  publishedIn: Date;
  publisher?: Publisher;
  description: string;
  tags: Tag[];
  authors: Author[];
  requirements: Benefit[];
  file?: File;
  cover?: ImageData;
  banner?: ImageData;
};

export async function registerBook(data: FormData) {
  return api.post<void>("/books", data, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

export async function updateBook(data: FormData) {
  return api.put<void>("/books", data, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

export async function fetchBooks(): Promise<Book[]> {
  return api
    .get<BookData[]>("books")
    .then((response) =>
      response.data
        .map((data) => new Book(data))
        .sort((a, b) => b.stars - a.stars)
    );
}

export async function fetchBook(id: number): Promise<Book> {
  return api
    .get<BookData>("books/" + id)
    .then((response) => new Book(response.data));
}

export async function fetchBooksByIds(ids: number[]): Promise<Book[]> {
  const books = await Promise.all(ids.map((id) => fetchBook(id)));
  return books;
}

export async function downloadBook(id: number): Promise<File> {
  return api
    .get<File>("books/" + id + "/download", {
      responseType: "blob",
    })
    .then(
      (response) => new File([response.data], response.headers["filename"])
    );
}
