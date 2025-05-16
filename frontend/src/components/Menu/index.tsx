import styles from "./index.module.scss";
import NetBooksSvg from "@components/NetBooksSvg";
import { FaSearch } from "react-icons/fa";

export default function Menu() {
  return (
    <nav className={styles.menu}>
      <div className={styles.container}>
        <div>
          <NetBooksSvg color="#8b5cf6" />
        </div>

        <div>
          <ul className={styles.links}>
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

        <div className={styles.buttons}>
          <button type="button" className={styles.button_light} /*onClick={}*/>
            Entrar
          </button>
          <button type="button" className={styles.button_dark} /*onClick={}*/>
            Cadastrar
          </button>
        </div>
        <div className={styles.buttons}>
          <span className={styles.user_name}>Nome Usuário</span>
          <button type="button" className={styles.button_light} /*onClick={}*/>
            Sair
          </button>
        </div>
      </div>
    </nav>
  );
}
