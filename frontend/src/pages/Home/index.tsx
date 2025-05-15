import styles from "./index.module.scss";
import Menu from "@components/Menu";

export default function Home() {
  return (
    <div className={styles.body}>
      <Menu />
      <section>
        <h1>Bem-vindo(a)</h1>
        <p>Aqui começa nossa aventura...</p>
      </section>
    </div>
  );
}
