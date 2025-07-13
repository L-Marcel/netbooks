import AuthGuard from "@components/Guards/AuthGuard";
import UserForm from "@components/User/UserForm";
import { Link } from "react-router-dom";

export default function Register() {
  return (
    <AuthGuard onlyUnauthenticated>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  return (
    <main className="py-10 flex flex-col w-full h-full min-h-screen justify-center items-center bg-gradient-to-br from-base-100 via-base-200 to-base-300">
      <section className="flex flex-col gap-6 w-full max-w-11/12 sm:max-w-sm">
        <header className="text-center text-base-content">
          <h1 className="text-3xl font-bold text-base-content">Criar conta</h1>
          <p>Cadastre-se para acessar todos os recursos</p>
        </header>
        <UserForm />
        <footer className="text-center">
          <p>
            Já tem uma conta?{" "}
            <Link to="/login" className="link link-primary">
              Conecte-se
            </Link>
          </p>
        </footer>
      </section>
    </main>
  );
}
