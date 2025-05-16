interface ButtonProps
  extends React.DetailedHTMLProps<
    React.ButtonHTMLAttributes<HTMLButtonElement>,
    HTMLButtonElement
  > {}

export default function Button({ className, ...props }: ButtonProps) {
  return <button className={`btn btn-primary ${className}`} {...props} />;
}
