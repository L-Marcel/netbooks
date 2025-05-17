import { FaMoon, FaSun } from "react-icons/fa";

export default function ThemeSwitcher() {
  return (
    <label className="swap swap-rotate">
      <input type="checkbox" className="theme-controller" value="dark" />
      <FaSun className="swap-off h-10 w-10 fill-current" />
      <FaMoon className="swap-on h-10 w-10 fill-current" />
    </label>
  );
}
