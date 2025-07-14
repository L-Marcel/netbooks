import React from "react";

export interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
    variant?: "default" | "like" | "dislike";
    size?: "default" | "sm" | "lg" | "icon";
    children?: React.ReactNode;
    className?: string;
}

function getButtonClass(
    variant: ButtonProps["variant"] = "default",
    size: ButtonProps["size"] = "default",
    className: string = ""
) {
    const baseClasses =
        "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-full text-sm font-medium ring-offset-background transition-colors transition-transform hover:-translate-y-1 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50";


    const variantClasses = {
        default: "bg-blue-600 text-white hover:bg-blue-700",
        like: "bg-green-600 text-white hover:bg-green-700",
        dislike: "bg-red-600 text-white hover:bg-red-700",
    };

    const sizeClasses = {
        default: "h-10 w-10 px-0 py-0",  // círculo perfeito
        sm: "h-9 w-9 px-0 py-0",
        lg: "h-12 w-12 px-0 py-0",
        icon: "h-10 w-10 px-0 py-0",
    };

    return `${baseClasses} ${variantClasses[variant]} ${sizeClasses[size]} ${className}`.trim();
}


export const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ variant = "default", size = "default", className = "", children, ...props }, ref) => {
    const classes = getButtonClass(variant, size, className);

    return (
        <button ref={ref} className={classes} {...props}>
            {children}
        </button>
    );
  }
);

Button.displayName = "Button";

// Função exportável para usar a mesma lógica de estilos fora do componente
export function buttonVariants({
    variant = "default",
    size = "default",
    className = "",
}: {
    variant?: ButtonProps["variant"];
    size?: ButtonProps["size"];
    className?: string;
} = {}) {
    return getButtonClass(variant, size, className);
}