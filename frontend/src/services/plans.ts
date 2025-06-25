import { Plan, PlanData } from "@models/plan";
import api from "./axios";

export async function fetchAvailablePlans(): Promise<Plan[]> {
  return api
    .get<PlanData[]>("/plans/availables")
    .then((response) => response.data.map((data) => new Plan(data)));
}
