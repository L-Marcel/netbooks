import useUser from "@stores/useUser";
import { ReactNode, useEffect } from "react";
import { useNavigate } from "react-router-dom";

interface Props {
  children: ReactNode;
}

export default function SubscriberGuard({ children }: Props) {
  const user = useUser((state) => state.user);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user || !user?.isSubscriber()) navigate("/subscribe");
  }, [user, navigate]);

  return user && user.isSubscriber() ? children : null;
}
