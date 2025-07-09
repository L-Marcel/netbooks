import api from "./axios";
import useAuth from "../stores/useUser";
import axios from "axios";
import { User, UserData } from "../models/user";
import useUser from "../stores/useUser";
import { Subscription, SubscriptionData } from "@models/subscription";
import { Benefit, BenefitData } from "@models/benefit";

export type UserLoginData = {
  email: string;
  password: string;
};

export type UserRegisterData = {
  name: string;
  avatar?: {
    blob: Blob;
    url: string;
    base64: string;
    filename?: string;
  };
  email: string;
  password: string;
  passwordConfirmation: string;
};

export async function login(data: UserLoginData) {
  return api.post<string>("/users/login", data).then((response) => {
    useAuth.getState().setToken(response.data);
  });
}

export async function logout() {
  const { setToken, setUser } = useAuth.getState();
  setToken(undefined);
  setUser(undefined);
}

export async function registerUser(data: FormData) {
  return api.post<void>("/users", data, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });
}

export async function fetchUpdatedUser(): Promise<void> {
  const token = useUser.getState().token;
  return fetchUser(token ?? "").then((user) => {
    useUser.getState().setUser(user);
  });
}

async function fetchSubscription(token: string): Promise<Subscription> {
  return api
    .get<SubscriptionData>("/subscriptions/me", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
    .then((response) => new Subscription(response.data));
}

async function fetchBenefits(token: string): Promise<Benefit[]> {
  return api
    .get<BenefitData[]>("/plans/me/benefits", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
    .then((response) => response.data.map((data) => data.name as Benefit));
}

export async function fetchUser(token: string): Promise<User> {
  return await Promise.all([
    await axios
      .get<UserData>("http://localhost:8080/users/me", {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      })
      .then((response) => response.data),
    await fetchBenefits(token).catch(() => [] as Benefit[]),
    await fetchSubscription(token).catch(() => undefined),
  ]).then(([data, benefits, subscription]) => {
    return new User(data, benefits, subscription);
  });
}

export async function switchAutomaticBilling() {
  return api.post("/users/switch-automatic-billing").then(async () => {
    const token = useAuth.getState().token;
    return fetchUser(token ?? "").then((user) => {
      useAuth.getState().setUser(user);
    });
  });
}
