import { ChangeEvent, FormEvent, useState } from "react";
import Input from "@components/Input";
import { Link, useNavigate } from "react-router-dom";
import { login, UserLoginData } from "../../services/user";
import { FaEnvelope, FaKey } from "react-icons/fa";
import AuthGuard from "@components/Guards/AuthGuard";
import useLoading from "../../hooks/useLoading";
import Loading from "@components/Loading";

export default function Login() {
  return (
    <AuthGuard onlyUnauthenticated>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const navigate = useNavigate();
  const loading = useLoading();
  const [data, setData] = useState<UserLoginData>({
    email: "",
    password: "",
  });

  const onChangeData = (e: ChangeEvent<HTMLInputElement>) => {
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));
  };

  const onSubmit = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    loading.start("login");
    login(data)
      .then(() => navigate("/"))
      .finally(() => loading.stop("login"));
  };

  return (
    <main className="py-10 flex flex-col w-full h-full min-h-screen justify-center items-center bg-gradient-to-br from-base-100 via-base-200 to-base-300">
      <section className="flex flex-col gap-6 w-full max-w-11/12 sm:max-w-sm">
        <header className="text-center text-base-content">
          <h1 className="text-3xl font-bold text-base-content">Entrar</h1>
          <p>Acesse sua conta agora mesmo</p>
        </header>
        <form className="flex flex-col gap-5 px-5 w-full" onSubmit={onSubmit}>
          <Input
            icon={FaEnvelope}
            label="E-mail"
            id="email"
            type="email"
            value={data.email}
            onChange={onChangeData}
            placeholder="marcela@email.com"
          />
          <Input
            icon={FaKey}
            label="Senha"
            id="password"
            type="password"
            value={data.password}
            onChange={onChangeData}
            placeholder="••••••••"
          />
          <button
            className="btn btn-primary"
            type="submit"
            disabled={loading.hasAny}
          >
            <Loading
              isLoading={loading.has("login")}
              loadingMessage="Entrando..."
              defaultMessage="Entrar"
            />
          </button>
        </form>
        <footer className="text-center">
          <p>
            Não tem uma conta?{" "}
            <Link to="/register" className="link link-primary">
              Cadastre-se
            </Link>
          </p>
        </footer>
      </section>
    </main>
  );
}
