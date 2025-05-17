import axios, { AxiosError } from "axios";
import useUser from "../stores/useUser";
import { logout } from "./user";

const api = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((request) => {
  const token = useUser.getState().token;

  if (token) {
    request.headers["Authorization"] = "Bearer " + token;
  }

  return request;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) logout();
    return Promise.reject(error.response?.data);
  }
);

export default api;
