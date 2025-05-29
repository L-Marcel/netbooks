import Logo from "@assets/netbooks.svg?react";
import { FaBars } from "react-icons/fa";
import useAuth from "../../stores/useUser";
import NavbarLinks from "./NavbarLinks";
import { Link } from "react-router-dom";
import Avatar from "./Avatar";

export default function Navbar() {
  const user = useAuth((state) => state.user);

  return (
    <nav className="navbar shadow-sm px-4 lg:px-8">
      <div className="navbar-start not-lg:gap-2">
        <div className="dropdown">
          <div
            tabIndex={0}
            role="button"
            className="btn btn-ghost btn-square lg:hidden"
          >
            <FaBars className="size-4" />
          </div>
          <ul
            tabIndex={0}
            className="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
          >
            <NavbarLinks user={user} />
          </ul>
        </div>
        <Logo className="h-8 w-18 lg:h-12 lg:w-28" />
      </div>
      <div className="navbar-center hidden lg:flex">
        <ul className="menu menu-horizontal px-1 items-center">
          <NavbarLinks user={user} />
        </ul>
      </div>
      {user ? (
        <div className="navbar-end flex gap-2">
          <Avatar />
        </div>
      ) : (
        <div className="navbar-end flex gap-4">
          <Link className="btn btn-primary" to="/login">
            Entrar
          </Link>
          <Link className="btn btn-outline btn-primary" to="/register">
            Cadastrar-se
          </Link>
        </div>
      )}
    </nav>
  );
}
