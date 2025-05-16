import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";
import "./index.css";

interface ButtonProps
  extends DetailedHTMLProps<
    ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {
  theme?: "dark" | "light";
  text: string;
}

export default function Button({
  theme = "dark",
  text,
  className,
  ...props
}: ButtonProps) {
  const classes = ["button", className];

  if (theme === "dark") classes.push("button-dark");
  if (theme === "light") classes.push("button-light");

  const finalClassName = classes.filter(Boolean).join(" ");
  return (
    <button className={finalClassName} {...props}>
      {text}
    </button>
  );
}
