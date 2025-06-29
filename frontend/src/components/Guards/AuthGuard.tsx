import { useNavigate } from "react-router-dom";
import useUser from "@stores/useUser";
import { ReactNode, useEffect } from "react";

interface Props {
  children: ReactNode;
  onlyUnauthenticated?: boolean;
}

export default function AuthGuard({
  children,
  onlyUnauthenticated = false,
}: Props) {
  const user = useUser((state) => state.user);
  const fetched = useUser((state) => state.fetched);
  const navigate = useNavigate();

  useEffect(() => {
    if (!!user === onlyUnauthenticated && fetched) {
      navigate(onlyUnauthenticated ? "/home" : "/login");
    }
  }, [user, onlyUnauthenticated, navigate, fetched]);

  return !!user !== onlyUnauthenticated && fetched ? children : null;
}
