import api from "./axios";
import { PlanEdition, PlanEditionData } from "@models/plan_edition";

export async function fetchUserPlanEdition(): Promise<PlanEdition> {
  return api
    .get<PlanEditionData>("/plans/editions/me")
    .then((response) => new PlanEdition(response.data));
}

export async function subscribe(id?: number): Promise<void> {
  if(!id) return;
  return api.post(`/subscriptions/subscribe/${id}`);
}