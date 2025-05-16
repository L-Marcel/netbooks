import "./index.css";

interface InputProps {
  type: string;
  id: string;
  value: string;
  setFunction: (value: string) => void;
  placeholder?: string;
}

export default function Input({
  type,
  id,
  value,
  setFunction,
  placeholder,
  ...props
}: InputProps) {
  return (
    <>
      <input
        type={type}
        name={id}
        id={id}
        value={value}
        onChange={(e) => setFunction(e.target.value)}
        placeholder={placeholder}
        required
        className="input"
        {...props}
      />
    </>
  );
}
