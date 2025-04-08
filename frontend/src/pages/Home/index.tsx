import styles from "./index.module.scss";

export default function Home() {
  return (
    <main className={styles.main}>
      <section className={styles.section}>
        <h1>Bem-vindo(a)</h1>
        <p>Aqui começa nossa aventura...</p>
      </section>
    </main>
  );
}
