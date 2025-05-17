import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { fetchUser, User } from "../services/user";

export type AuthStore = {
  token?: string;
  user?: User;
  setToken: (token?: string) => void;
  setUser: (user?: User) => void;
};

const useAuth = create<AuthStore>()(
  persist(
    (set) => ({
      setToken: (token?: string) => {
        set({ token });
        if (token) {
          fetchUser(token).then((user) => {
            set({ user });
          });
        }
      },
      setUser: (user?: User) => set({ user }),
    }),
    {
      name: "netbooks@auth",
      storage: createJSONStorage(() => localStorage),
      partialize: (state: AuthStore) => ({ token: state.token }),
      onRehydrateStorage: () => {
        return (state?: AuthStore, error?: unknown) => {
          if (state?.token && !error) {
            fetchUser(state.token).then((user) => {
              state?.setUser(user);
            });
          }
        };
      },
    }
  )
);

export default useAuth;
