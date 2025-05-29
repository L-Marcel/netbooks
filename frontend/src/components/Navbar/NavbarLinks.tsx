import { NavLink } from "react-router-dom";
import { User } from "../../services/user";
import { FaBook, FaCompass, FaHome } from "react-icons/fa";

interface Props {
  user?: User;
}

export default function NavbarLinks({ user }: Props) {
  return (
    <>
      <li>
        <NavLink
          className={({ isActive }) =>
            "text-base-content no-underline hover:underline font-bold transition" +
            (isActive ? " text-primary" : "")
          }
          to="/home"
        >
          <FaHome /> Início
        </NavLink>
      </li>
      {user && (
        <li>
          <NavLink
            className={({ isActive }) =>
              "text-base-content no-underline hover:underline font-bold transition" +
              (isActive ? " text-primary" : "")
            }
            to="/bookcase"
          >
            <FaBook /> Estante
          </NavLink>
        </li>
      )}
      <li>
        <NavLink
          className={({ isActive }) =>
            "text-base-content no-underline hover:underline font-bold transition" +
            (isActive ? " text-primary" : "")
          }
          to="/explore"
        >
          <FaCompass /> Encontrar
        </NavLink>
      </li>
    </>
  );
}
