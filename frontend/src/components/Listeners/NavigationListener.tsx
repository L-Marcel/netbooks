import { useLoading } from "@stores/useLoading";
import { useEffect } from "react";
import { useLocation } from "react-router-dom";

export default function NavigationListener() {
  const location = useLocation();
  const clearLoading = useLoading((state) => state.clear);

  useEffect(() => clearLoading(), [location, clearLoading]);

  return null;
}
