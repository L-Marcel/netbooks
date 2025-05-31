import { Book, Tag } from "@models/book";
import api from "./axios";

export async function fetchBooks(): Promise<Book[]> {
  return api
    .get<Book[]>("books")
    .then((response) =>
      response.data
        .map((data) => new Book(data))
        .sort((a, b) => b.stars - a.stars)
    );
}

export async function fetchTags(): Promise<Tag[]> {
  return api.get<Tag[]>("tags").then((response) => response.data);
}
