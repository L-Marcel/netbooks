import useUser from "@stores/useUser";
import { ReactNode, useEffect } from "react";
import { useNavigate } from "react-router-dom";

interface Props {
  children: ReactNode;
}

export default function SubscriberGuard({ children }: Props) {
  const user = useUser((state) => state.user);
  const fetched = useUser((state) => state.fetched);
  const navigate = useNavigate();

  useEffect(() => {
    if (fetched && !(user && (user?.isSubscriber() || user?.isAdmin())))
      navigate("/subscribe");
  }, [user, fetched, navigate]);

  return user && (user.isSubscriber() || user.isAdmin()) ? children : null;
}
