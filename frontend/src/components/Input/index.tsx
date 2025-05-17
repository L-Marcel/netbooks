interface InputProps
  extends React.DetailedHTMLProps<
    React.InputHTMLAttributes<HTMLInputElement>,
    HTMLInputElement
  > {
  label?: string;
}

export default function Input({
  id,
  label,
  className = "",
  ...props
}: InputProps) {
  return (
    <label className="flex flex-col gap-2 w-full text-base-content">
      <span>{label}</span>
      <input
        className={`input focus-within:input-primary w-full ${className}`}
        id={id}
        name={id}
        {...props}
      />
    </label>
  );
}
