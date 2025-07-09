import { useLoading } from "@stores/useLoading";
import { DetailedHTMLProps, InputHTMLAttributes } from "react";

export default function Input({
  disabled,
  ...props
}: DetailedHTMLProps<InputHTMLAttributes<HTMLInputElement>, HTMLInputElement>) {
  const loadingHasAny = useLoading((state) => state.hasAny);

  return <input disabled={loadingHasAny || disabled} {...props} />;
}
