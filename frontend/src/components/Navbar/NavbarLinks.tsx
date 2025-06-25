import { NavLink } from "react-router-dom";
import { FaBook, FaCompass, FaFireAlt, FaHome, FaSearch } from "react-icons/fa";
import { User } from "@models/user";

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
      {user?.isSubscriber() ? (
        <>
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
          <li>
            <NavLink
              className={({ isActive }) =>
                "text-base-content no-underline hover:underline font-bold transition" +
                (isActive ? " text-primary" : "")
              }
              to="/explore"
            >
              <FaSearch /> Encontrar
            </NavLink>
          </li>
          <li>
            <NavLink
              className={({ isActive }) =>
                "text-base-content no-underline hover:underline font-bold transition" +
                (isActive ? " text-primary" : "")
              }
              to="/match"
            >
              <FaCompass /> sortear
            </NavLink>
          </li>
        </>
      ) : (
        <li>
          <NavLink
            className={({ isActive }) =>
              "text-base-content hover:text-warning focus:!text-warning focus-visible:text-warning no-underline hover:underline font-bold transition" +
              (isActive ? " text-warning" : "")
            }
            to="/subscribe"
          >
            <FaFireAlt /> Assinar
          </NavLink>
        </li>
      )}
    </>
  );
}
