import "./index.css";
import NetBooksSvg from "@components/NetBooksSvg";
import { FaSearch } from "react-icons/fa";
import Button from "@components/Button/index";

export default function Navbar() {
  const isAuthenticaded = false; //useUser

  return (
    <nav className="menu">
      <div className="container">
        <div className="left">
          <NetBooksSvg color="#8b5cf6" />
        </div>

        <div className="center">
          <ul className="links">
            <li>
              <a href="">
                <FaSearch />
              </a>
            </li>
            <li>
              <a href="inicio">Início</a>
            </li>
            {isAuthenticaded ? (
              <li>
                <a href="estante">Estante</a>
              </li>
            ) : (
              ""
            )}

            <li>
              <a href="sortear">Sortear</a>
            </li>
          </ul>
        </div>

        <div className="right">
          {isAuthenticaded ? (
            <div className="buttons">
              <span className="user_name">Nome Usuário</span>
              <Button
                text="Sair"
                theme="light"
                /*onClick={}*/
              />
            </div>
          ) : (
            <div className="buttons">
              <Button
                text="Entrar"
                theme="light"
                /*onClick={}*/
              />
              <Button
                text="Cadastrar"
                theme="dark"
                /*onClick={}*/
              />
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}
