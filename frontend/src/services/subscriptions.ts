import { Subscription, SubscriptionData } from "@models/subscription";
import api from "./axios";
import { RenewDetails, RenewDetailsData } from "@models/renew_details";

export async function fetchSubscription(): Promise<Subscription> {
  return api
    .get<SubscriptionData>("/subscriptions/me")
    .then((response) => new Subscription(response.data));
}

export async function fetchRenewDetails(): Promise<RenewDetails> {
  return api
    .get<RenewDetailsData>("/subscriptions/me/renew/details")
    .then((response) => new RenewDetails(response.data));
}

export async function fetchNextSubscription(): Promise<Subscription> {
  return api
    .get<SubscriptionData>("/subscriptions/me/next")
    .then((response) => new Subscription(response.data));
}

export async function closeNextSubscriptions(): Promise<Subscription> {
  return api
    .post<SubscriptionData>("/subscriptions/me/next/cancel")
    .then((response) => new Subscription(response.data));
}

export async function subscribe(id: number): Promise<void> {
  return api.post(`/subscriptions/subscribe/${id}`);
}
