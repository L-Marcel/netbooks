import axios, { AxiosError } from "axios";
import useUser from "../stores/useUser";
import { logout } from "./user";

type ExpectedApiValidationError = {
  field: string;
  messages: {
    error: boolean;
    content: string;
  }[];
};

type ExpectedApiError = {
  status: number;
  error: string;
  errors?: ExpectedApiValidationError[];
};

export type Validation = {
  error: boolean;
  content: string;
};

export type ValidationError = {
  [key in string]: Validation[];
};

export type ApiError =
  | {
      type: "default";
      status: number;
      error: string;
    }
  | ({
      type: "validation";
      status: number;
    } & ValidationError);

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

    if (error.response?.data) {
      const apiError = error.response?.data as ExpectedApiError;

      if (apiError.errors) {
        return Promise.reject(
          apiError.errors.reduce(
            (previous, current) => {
              return {
                ...previous,
                [current.field]: current.messages as Validation[],
              } as ApiError;
            },
            {
              type: "validation",
              status: apiError.status,
            } as ApiError
          )
        );
      } else
        return Promise.reject({
          type: "default",
          error: apiError.error,
          status: apiError.status,
        } as ApiError);
    }

    return Promise.reject({
      status: error.response?.status,
      data: error.response?.data,
      error: "Erro inesperado!",
      type: "default",
    } as ApiError);
  }
);

export default api;
