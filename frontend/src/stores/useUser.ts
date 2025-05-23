import { create } from "zustand";
import { persist, createJSONStorage } from "zustand/middleware";
import { fetchUser, User } from "../services/user";

export type UserStore = {
  token?: string;
  user?: User;
  setToken: (token?: string) => void;
  setUser: (user?: User) => void;
};

const useUser = create<UserStore>()(
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
      partialize: (state: UserStore) => ({ token: state.token }),
      onRehydrateStorage: () => {
        return (state?: UserStore, error?: unknown) => {
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

export default useUser;
