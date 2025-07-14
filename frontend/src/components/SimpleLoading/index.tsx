import React from "react";
import styles from "@components/SimpleLoading/index.module.scss"

interface LoaderProps {
  text?: string;
}

const Loader: React.FC<LoaderProps> = ({ text = "Carregando..." }) => {
  return (
    <div className={styles.loader_container}>
      <div className={styles.spinner} />
      <span className={styles.loader_text}>{text}</span>
    </div>
  );
};

export default Loader;
