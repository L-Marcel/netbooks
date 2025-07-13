import { Author, AuthorData } from "@models/author";
import { GenericAbortSignal } from "axios";
import api from "./axios";

export async function searchAuthors(
  query: string,
  signal: GenericAbortSignal
): Promise<Author[]> {
  return api
    .get<AuthorData[]>("authors/search?name=" + query, {
      signal,
    })
    .then((response) => response.data.map((data) => new Author(data)));
}
