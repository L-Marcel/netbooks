import { ButtonHTMLAttributes, DetailedHTMLProps } from "react";
import styles from "./index.module.scss"; 

interface ButtonProps extends DetailedHTMLProps<ButtonHTMLAttributes<HTMLButtonElement>, HTMLButtonElement> {
  theme?: "dark" | "light";
  text: string;
}

export default function Button({
  theme = "dark",
  text,
  className,
  ...props
}: ButtonProps) {
  const classes = [styles.button, className];

  if (theme === "dark") classes.push(styles.dark);
  if (theme === "light") classes.push(styles.light);

  const finalClassName = classes.filter(Boolean).join(" ");
  return <button className={finalClassName} {...props}>{text}</button>;
}