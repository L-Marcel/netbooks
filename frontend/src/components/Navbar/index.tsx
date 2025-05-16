import NetBooksSvg from "@components/NetBooksSvg";
import { FaSearch } from "react-icons/fa";
import Button from "@components/Button/index";
import { Link, NavLink } from "react-router-dom";

export default function Navbar() {
  const isAuthenticaded = false; //useUser

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
                (isActive? " text-primary":"")
              }
              to="/home"
            >
              Início
            </NavLink>
          </li>
          {isAuthenticaded && (
            <li>
              <NavLink
                className={({ isActive }) =>
                  "text-base-content no-underline hover:underline font-bold transition" +
                  (isActive? " text-primary":"")
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
                (isActive? " text-primary":"")
              }
              to="/sort"
            >
              Sortear
            </NavLink>
          </li>
        </ul>
        <div className="flex gap-2.5">
          {isAuthenticaded ? (
            <>
              <p className="flex items-center text-base-content">Usuário</p>
              <Button>Sair</Button>
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
