import { useLoading } from "@stores/useLoading";
import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";

export default function Button({
  disabled,
  ...props
}: DetailedHTMLProps<
  ButtonHTMLAttributes<HTMLButtonElement>,
  HTMLButtonElement
>) {
  const hasAnyLoading = useLoading((state) => state.hasAny);

  return (
    <button disabled={hasAnyLoading || disabled} type="button" {...props} />
  );
}
