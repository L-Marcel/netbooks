import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { fetchUser } from "@services/user";
import { User } from "../models/user";

export type UserStore = {
  token?: string;
  user?: User;
  fetched: boolean;
  setToken: (token?: string) => void;
  setUser: (user?: User) => void;
  setFetched: (fetched: boolean) => void;
};

const useUser = create<UserStore>()(
  persist(
    (set) => ({
      fetched: false,
      setToken: (token?: string) => {
        set({ token });
        if (token) {
          fetchUser(token)
            .then((user) => {
              set({ user });
            })
            .finally(() => {
              set({ fetched: true });
            });
        } else set({ fetched: true });
      },
      setUser: (user?: User) => set({ user }),
      setFetched: (fetched: boolean) => set({ fetched }),
    }),
    {
      name: "netbooks@auth",
      storage: createJSONStorage(() => localStorage),
      partialize: (state: UserStore) => ({ token: state.token }),
      onRehydrateStorage: () => {
        return (state?: UserStore, error?: unknown) => {
          if (state?.token && !error) {
            fetchUser(state.token)
              .then((user) => {
                state?.setUser(user);
              })
              .finally(() => {
                state?.setFetched(true);
              });
          }
        };
      },
    }
  )
);

export default useUser;
