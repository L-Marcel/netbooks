import NetBooksSvg from "@components/NetBooksSvg";
import { FaSearch } from "react-icons/fa";
import { Link, NavLink } from "react-router-dom";
import useAuth from "../../stores/useUser";
import { logout } from "../../services/auth";

export default function Navbar() {
  const user = useAuth((state) => state.user);

  return (
    <nav className="bg-base-100 w-full sticky top-0">
      <div className="flex items-center justify-between py-1.5 pr-12 pl-2.5 w-full">
        <div className="flex items-center">
          <NetBooksSvg color="#8b5cf6" />
        </div>
        <ul className="list-none text-lg flex gap-8">
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
        </ul>
        <div className="flex gap-2.5">
          {user ? (
            <>
              <p className="flex items-center text-base-content">{user.name}</p>
              <button onClick={logout} className="btn btn-error">Sair</button>
            </>
          ) : (
            <>
              <Link className="btn btn-primary" to="/login">
                Entrar
              </Link>
              <Link className="btn btn-soft btn-primary" to="/register">
                Cadastrar-se
              </Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
