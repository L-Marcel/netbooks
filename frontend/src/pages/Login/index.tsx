import styles from "./index.module.scss";
import useUser from "../../stores/useUser";
import { useState } from "react";
import Button from "@components/Button";
import Input from "@components/Input";

export default function Login() {
  //const login = useUser();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!email || !password) {
      alert("preencha os campos");
      // Alert para preenchimento de campos
      return;
    }

    setIsLoading(true);
    try {
      alert("ok");
      // Alert de sucesso e redirecionamento de página
    } catch (error) {
      // Alert de erro
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.box}>
        <div className={styles.text}>
          <h1>Entrar</h1>
          <p>Entre para acessar sua conta</p>
        </div>
        <form onSubmit={handleSubmit}>
          <div className={styles.form_group}>
            <label htmlFor="email">Email</label>
            <Input
              type="email"
              id="email"
              value={email}
              setFunction={setEmail}
              placeholder="seu@email.com"
            />
          </div>

          <div className={styles.form_group}>
            <label htmlFor="password">Senha</label>
            <Input
              type="password"
              id="password"
              value={password}
              setFunction={setPassword}
              placeholder="*****"
            />
          </div>

          <div className={styles.form_group}>
            <Button
              type="submit"
              theme="dark"
              text={isLoading ? "Entrando..." : "Entrar"}
              disabled={isLoading}
            />
          </div>
        </form>
        <div className={styles.hasSubscription}>
          <p>
            Não tem uma conta?{" "}
            <a href="#cadastro" className={styles.subscription}>
              Cadastre-se
            </a>
          </p>
        </div>
      </div>
    </div>
  );
}
