import { Classification, ClassificationData } from "@models/classification";
import api from "./axios";

export async function fetchClassification(id: number): Promise<Classification> {
  return api
    .get<ClassificationData>("books/" + id + "/classification")
    .then((response) => new Classification(response.data));
}

export async function updateClassification(
  id: number,
  value: number
): Promise<Classification> {
  return api
    .post<ClassificationData>("books/" + id + "/classification", { value })
    .then((response) => new Classification(response.data));
}
