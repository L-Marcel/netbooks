import { Tag, TagData } from "@models/tag";
import { GenericAbortSignal } from "axios";
import api from "./axios";

export async function fetchTags(): Promise<Tag[]> {
  return api.get<Tag[]>("tags").then((response) => response.data);
}

export async function searchTags(
  query: string,
  signal: GenericAbortSignal
): Promise<Tag[]> {
  return api
    .get<TagData[]>("tags/search?name=" + query, {
      signal,
    })
    .then((response) => response.data.map((data) => new Tag(data)));
}
