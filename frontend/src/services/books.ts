import { Book, BookData } from "@models/book";
import api from "./axios";
import { ImageData } from "./image";
import { Benefit } from "@models/benefit";
import { Author } from "@models/author";

export type BookRegisterData = {
  isbn?: number;
  title: string;
  publishedIn: Date;
  publisher: string;
  description: string;
  tags: string[];
  authors: Author[];
  requirements: Benefit[];
  file?: File;
  cover?: ImageData;
  banner?: ImageData;
};

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

export async function downloadBook(id: number): Promise<File> {
  return api
    .get<File>("books/" + id + "/download", {
      responseType: "blob",
    })
    .then(
      (response) => new File([response.data], response.headers["filename"])
    );
}
