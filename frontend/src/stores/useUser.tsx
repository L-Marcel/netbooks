export type User = {
  name: string;
};

export type UserStore = {
  user?: User;
  isAuthenticated: boolean;
  setUser: (user?: User) => void;
};

const useUser = {};

export default useUser;
