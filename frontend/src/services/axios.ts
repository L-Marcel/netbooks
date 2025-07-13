import axios, { AxiosError } from "axios";
import useUser from "../stores/useUser";
import { logout } from "./user";

export type ExpectedApiValidationError = {
  field: string;
  messages: {
    error: boolean;
    content: string;
  }[];
};

export type ExpectedApiError = {
  status: number;
  message?: string;
  errors?: ExpectedApiValidationError[];
};

export type Validation = {
  error: boolean;
  content: string;
};

export type ValidationErrors = {
  [key in string]: Validation[];
};

export type ValidationError = {
  type: "validation";
  status: number;
} & ValidationErrors;

export type HttpError = {
  type: "default";
  status: number;
  error: string;
};

export type ApiError = HttpError | ValidationError;

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
              } as ValidationError;
            },
            {
              type: "validation",
              status: apiError.status,
            } as ValidationError
          )
        );
      } else
        return Promise.reject({
          type: "default",
          error: apiError.message,
          status: apiError.status,
        } as HttpError);
    }

    return Promise.reject({
      status: error.response?.status,
      data: error.response?.data,
      error: "Erro inesperado!",
      type: "default",
    } as HttpError);
  }
);

export default api;
