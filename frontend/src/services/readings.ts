import api from "./axios";
import { Reading } from "@models/reading";

export async function fetchReadings(): Promise<Reading[]> {
  return api
    .get<Reading[]>("readings/me")
    .then((response) => response.data.map((data) => new Reading(data)));
}

export async function fetchReadingsOfBook(book: number): Promise<Reading[]> {
  return api
    .get<Reading[]>("readings/me/" + book)
    .then((response) => response.data.map((data) => new Reading(data)));
}

export async function startReadingsOfBook(book: number): Promise<Reading> {
  return api
    .post<Reading>("readings/me/" + book)
    .then((response) => new Reading(response.data));
}

export async function finishReading(id: number): Promise<void> {
  return api.post("readings/" + id + "/finish");
}

export async function fetchReading(id: number): Promise<Reading> {
  return api
    .get<Reading>("readings/" + id)
    .then((response) => new Reading(response.data));
}

export async function fetchReadingByBook(book: number): Promise<Reading> {
  return api
    .get<Reading>("readings/books/" + book)
    .then((response) => new Reading(response.data));
}
