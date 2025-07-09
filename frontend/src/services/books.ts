import { Book, BookData, Tag } from "@models/book";
import api from "./axios";

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

export async function downloadBook(
  id: number
): Promise<File> {
  return api
    .get<File>("books/" + id + "/download", {
      responseType: "blob",
    })
    .then(
      (response) => new File([response.data], response.headers["filename"])
    );
}

export async function fetchTags(): Promise<Tag[]> {
  return api.get<Tag[]>("tags").then((response) => response.data);
}
