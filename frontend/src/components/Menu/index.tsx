import "./index.css";
import NetBooksSvg from "@components/NetBooksSvg";
import { FaSearch } from "react-icons/fa";

export default function Menu() {
  return (
    <nav className="menu">
      <div className="container">
        <div>
          <NetBooksSvg color="#8b5cf6" />
        </div>

        <div>
          <ul className="links">
            <li>
              <a href="">
                <FaSearch />
              </a>
            </li>
            <li>
              <a href="inicio">Início</a>
            </li>
            <li>
              <a href="estante">Estante</a>
            </li>
            <li>
              <a href="sortear">Sortear</a>
            </li>
          </ul>
        </div>

        <div className="buttons">
          <button type="button" className="button_light" /*onClick={}*/>
            Entrar
          </button>
          <button type="button" className="button_dark" /*onClick={}*/>
            Cadastrar
          </button>
        </div>
        <div className="buttons">
          <span className="user_name">Nome Usuário</span>
          <button type="button" className="button_light" /*onClick={}*/>
            Sair
          </button>
        </div>
      </div>
    </nav>
  );
}
