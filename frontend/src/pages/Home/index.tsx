import styles from "./index.module.scss";
import Menu from "@components/Menu";

export default function Home() {
  return (
    <div className={styles.main}>
      <Menu />
    </div>
  );
}
