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

export async function registerUser(data: UserRegisterData) {
  return api.post<void>("/users", {
    ...data,
    avatar: data.avatar?.base64,
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
  return Promise.all([
    axios
      .get<UserData>("http://localhost:8080/users/me", {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + token,
        },
      })
      .then((response) => response.data),
    fetchBenefits(token).catch(() => []),
    fetchSubscription(token).catch(() => undefined),
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
