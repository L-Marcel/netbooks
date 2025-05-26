import NetBooksSvg from "@components/NetBooksSvg";
import { FaBars } from "react-icons/fa";
import useAuth from "../../stores/useUser";
import NavbarLinks from "./NavbarLinks";
import { logout } from "../../services/user";
import { Link } from "react-router-dom";

export default function Navbar() {
  const user = useAuth((state) => state.user);

  return (
    <nav className="navbar shadow-sm">
      <div className="navbar-start">
        <div className="dropdown">
          <div tabIndex={0} role="button" className="btn btn-ghost lg:hidden">
            <FaBars />
          </div>
          <ul
            tabIndex={0}
            className="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
          >
            <NavbarLinks user={user} />
          </ul>
        </div>
        <a className="btn btn-ghost text-xl">
          <NetBooksSvg color="#8b5cf6" />
        </a>
      </div>
      <div className="navbar-center hidden lg:flex">
        <ul className="menu menu-horizontal px-1">
          <NavbarLinks user={user} />
        </ul>
      </div>
      <div className="navbar-end flex gap-4">
        {user ? (
          <>
            <p className="flex items-center text-base-content">{user.name}</p>
            <button type="button" onClick={logout} className="btn btn-error">
              Sair
            </button>
          </>
        ) : (
          <>
            <Link className="btn btn-primary" to="/login">
              Entrar
            </Link>
            <Link className="btn btn-outline btn-primary" to="/register">
              Cadastrar-se
            </Link>
          </>
        )}
      </div>
    </nav>
  );
}

{
  /* <nav className="bg-base-100 w-full sticky top-0">
      <div className="flex items-center justify-between py-1.5 pr-12 pl-2.5 w-full">
        <div className="flex items-center">
         
        </div>
        <ul className="list-none text-lg flex gap-8">
          
        </ul>
        <div className="flex gap-2.5">
          
        </div>
      </div>
    </nav> */
}
