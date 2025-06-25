import { Subscription, SubscriptionData } from "@models/subscription";
import api from "./axios";

export async function fetchSubscription(): Promise<Subscription> {
  return api
    .get<SubscriptionData>("/subscriptions/me")
    .then((response) => new Subscription(response.data));
}

export async function subscribe(id: number): Promise<void> {
  return api.post(`/subscriptions/subscribe/${id}`);
}

export async function unsubscribe(): Promise<void> {
  return api.post("/subscriptions/unsubscribe");
}

export async function renewSubscription(): Promise<void> {
  return api.post("/subscriptions/renew");
}
