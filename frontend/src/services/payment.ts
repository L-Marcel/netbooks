import { Payment, PaymentData } from "@models/payment";
import api from "./axios";

export async function fetchPayments(): Promise<Payment[]> {
  return api
    .get<PaymentData[]>("/subscriptions/me/payments")
    .then((response) => response.data.map((data) => new Payment(data)));
}

export async function pay(): Promise<void> {
  return api.post("/subscriptions/me/payments/pay");
}
