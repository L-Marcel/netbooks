import { GenericAbortSignal } from "axios";
import api from "./axios";
import { Publisher, PublisherData } from "@models/publisher";

export async function searchPublishers(
  query: string,
  signal: GenericAbortSignal
): Promise<Publisher[]> {
  return api
    .get<PublisherData[]>("publishers/search?name=" + query, {
      signal,
    })
    .then((response) => response.data.map((data) => new Publisher(data)));
}
