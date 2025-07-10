import Button from "@components/Button";
import { logout } from "@services/user";
import useUser from "@stores/useUser";
import { useState } from "react";
import {
  FaEdit,
  FaFireAlt,
  FaRegCreditCard,
  FaSignOutAlt,
} from "react-icons/fa";
import { NavLink } from "react-router-dom";

export default function Avatar() {
  const [imageError, setImageError] = useState(false);
  const user = useUser((state) => state.user);

  const onImageError = () => setImageError(true);

  return (
    <div className="dropdown dropdown-end">
      <div
        tabIndex={0}
        role="button"
        className="btn btn-outline ring-offset-2 ring-offset-base-100 ring-primary focus-visible:ring-2 focus:ring-2 focus-visible:btn-primary focus:btn-primary hover:btn-primary gap-2 rounded-4xl not-md:btn-circle md:pr-1"
      >
        <p className="not-md:hidden flex items-center text-sm font-bold text-base-content">
          {user?.name}
        </p>
        {imageError ? (
          <div className="avatar avatar-placeholder">
            <div className="bg-base-300 text-neutral-content w-8 rounded-full">
              <span className="text-lg">{user?.getInitials()}</span>
            </div>
          </div>
        ) : (
          <div className="avatar">
            <div className="w-8 rounded-full">
              <img src={user?.getAvatarUrl()} onError={onImageError} />
            </div>
          </div>
        )}
      </div>
      <ul
        tabIndex={0}
        className="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
      >
        <li>
          <NavLink
            className={({ isActive }) =>
              "text-base-content no-underline hover:underline font-bold transition" +
              (isActive ? " text-primary" : "")
            }
            to="/profile/edit"
          >
            <FaEdit /> Editar perfil
          </NavLink>
          <NavLink
            className={({ isActive }) =>
              "text-base-content no-underline hover:underline font-bold transition" +
              (isActive ? " text-primary" : "")
            }
            to="/subscribe"
          >
            <FaFireAlt /> Gerenciar assinatura
          </NavLink>
          <NavLink
            className={({ isActive }) =>
              "text-base-content no-underline hover:underline font-bold transition" +
              (isActive ? " text-primary" : "")
            }
            to="/billing"
          >
            <FaRegCreditCard /> Dados de cobrança
          </NavLink>
          <Button
            className="text-base-content no-underline hover:underline font-bold transition"
            onClick={logout}
          >
            <FaSignOutAlt /> Sair
          </Button>
        </li>
      </ul>
    </div>
  );
}
