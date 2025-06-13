import { Subscription, SubscriptionData } from "@models/subscription";
import api from "./axios";

export async function fetchSubscription(): Promise<Subscription> {
  return api
    .get<SubscriptionData>("/subscriptions/me")
    .then((response) => new Subscription(response.data));
}

export async function subscribe(id?: number): Promise<void> {
  if (!id) return;
  return api.post(`/subscriptions/subscribe/${id}`);
}
