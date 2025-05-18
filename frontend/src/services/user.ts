import { UUID } from "crypto";
import api from "./axios";
import useAuth from "../stores/useUser";
import axios from "axios";

enum Role {
  ADMINISTRATOR = "ADMINISTRATOR",
  SUBSCRIBER = "SUBSCRIBER",
  UNKNOWN = "UNKNOWN",
}

export type User = {
  uuid: UUID;
  name: string;
  email: string;
  roles: Role[];
};

export type UserLoginData = {
  email: string;
  password: string;
};

export type UserRegisterData = {
  name: string;
  avatar?: {
    url: string;
    base64: string;
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

export async function register(data: UserRegisterData) {
  return api.post<void>("/users", {
    ...data,
    passwordConfirmation: undefined,
    avatar: data.avatar?.base64,
  });
}

export async function logout() {
  const { setToken, setUser } = useAuth.getState();
  setToken(undefined);
  setUser(undefined);
}

export async function fetchUser(token: string): Promise<User> {
  return axios
    .get<User>("http://localhost:8080/users/me", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
    .then((response) => response.data);
}
