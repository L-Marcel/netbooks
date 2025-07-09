import { GenericAbortSignal } from "axios";
import api from "./axios";
import { Reading, ReadingData } from "@models/reading";

export async function fetchReadings(): Promise<Reading[]> {
  return api
    .get<ReadingData[]>("readings/me")
    .then((response) => response.data.map((data) => new Reading(data)));
}

export async function fetchReadingsOfBook(book: number): Promise<Reading[]> {
  return api
    .get<ReadingData[]>("readings/me/" + book)
    .then((response) => response.data.map((data) => new Reading(data)));
}

export async function startReadingsOfBook(book: number): Promise<Reading> {
  return api
    .post<ReadingData>("readings/me/" + book)
    .then((response) => new Reading(response.data));
}

export async function finishReading(id: number): Promise<void> {
  return api.post("readings/" + id + "/finish");
}

export async function fetchReading(id: number): Promise<Reading> {
  return api
    .get<ReadingData>("readings/" + id)
    .then((response) => new Reading(response.data));
}

export async function fetchReadingContent(
  id: number,
  page: number,
  signal: GenericAbortSignal
): Promise<File> {
  return api
    .get<File>("readings/" + id + "/content?page=" + page, {
      responseType: "blob",
      signal,
    })
    .then(
      (response) => new File([response.data], response.headers["filename"])
    );
}

export async function fetchReadingByBook(book: number): Promise<Reading> {
  return api
    .get<ReadingData>("readings/books/" + book)
    .then((response) => new Reading(response.data));
}
