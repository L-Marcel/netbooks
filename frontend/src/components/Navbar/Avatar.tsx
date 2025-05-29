import { logout } from "@services/user";
import useUser from "@stores/useUser";
import { FaEdit, FaSignOutAlt } from "react-icons/fa";

export default function Avatar() {
  const user = useUser((state) => state.user);

  return (
    <div className="dropdown dropdown-end">
      <div
        tabIndex={0}
        role="button"
        className="btn btn-outline ring-offset-2 ring-offset-base-100 ring-primary focus-visible:ring-2 focus:ring-2 focus-visible:btn-primary focus:btn-primary hover:btn-primary gap-2 rounded-4xl md:pr-1"
      >
        <p className="not-md:hidden flex items-center text-sm font-bold text-base-content">
            {user?.name}
        </p>
        <div className="avatar">
          <div className="w-8 rounded-full">
            <img src="https://img.daisyui.com/images/profile/demo/spiderperson@192.webp" />
          </div>
        </div>
      </div>
      <ul
        tabIndex={0}
        className="menu menu-sm dropdown-content bg-base-100 rounded-box z-1 mt-3 w-52 p-2 shadow"
      >
        <li>
          <button type="button">
            <FaEdit /> Editar perfil
          </button>
          <button type="button" onClick={logout}>
            <FaSignOutAlt /> Sair
          </button>
        </li>
      </ul>
    </div>
  );
}
