import api from "./axios";
import useAuth from "../stores/useUser";
import axios from "axios";
import { User, UserData } from "../models/user";

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

export async function fetchUser(token: string): Promise<User> {
  return axios
    .get<UserData>("http://localhost:8080/users/me", {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + token,
      },
    })
    .then((response) => new User(response.data));
}
