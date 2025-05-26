import { FaSearch } from "react-icons/fa";
import { NavLink } from "react-router-dom";
import { User } from "../../services/user";

interface Props {
  user?: User;
}

export default function NavbarLinks({ user }: Props) {
  return (
    <>
      <li className="mt-1">
        <FaSearch />
      </li>
      <li>
        <NavLink
          className={({ isActive }) =>
            "text-base-content no-underline hover:underline font-bold transition" +
            (isActive ? " text-primary" : "")
          }
          to="/home"
        >
          Início
        </NavLink>
      </li>
      {user && (
        <li>
          <NavLink
            className={({ isActive }) =>
              "text-base-content no-underline hover:underline font-bold transition" +
              (isActive ? " text-primary" : "")
            }
            to="/books"
          >
            Estante
          </NavLink>
        </li>
      )}
      <li>
        <NavLink
          className={({ isActive }) =>
            "text-base-content no-underline hover:underline font-bold transition" +
            (isActive ? " text-primary" : "")
          }
          to="/sort"
        >
          Sortear
        </NavLink>
      </li>
    </>
  );
}
