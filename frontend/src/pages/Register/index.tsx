import { ChangeEvent, useState } from "react";
import Input from "@components/Input";
import { Link, useNavigate } from "react-router-dom";
import { register, UserRegisterData } from "../../services/user";
import ImageInput from "@components/Input/FileInput";
import { FaUpload } from "react-icons/fa";

export default function Register() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [data, setData] = useState<UserRegisterData>({
    name: "",
    email: "",
    password: "",
    passwordConfirmation: "",
  });

  const onChangeData = (e: ChangeEvent<HTMLInputElement>) => {
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));
  };

  const onClearAvatar = () => {
    setData((data) => ({
      ...data,
      avatar: undefined,
    }));
  };

  const onLoadAvatar = (base64: string, blob: Blob, filename?: string) => {
    const url = URL.createObjectURL(blob);
    setData((data) => ({
      ...data,
      avatar: {
        url,
        base64,
        filename,
      },
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);
    register(data)
      .then(() => {
        navigate("/login");
      })
      .finally(() => {
        setIsLoading(false);
      });
  };

  return (
    <main className="flex flex-col w-full h-screen justify-center items-center">
      <section className="flex flex-col gap-6 w-full max-w-11/12 sm:max-w-sm">
        <header className="text-center text-base-content">
          <h1 className="text-3xl font-bold text-base-content">Criar conta</h1>
          <p>Cadastre-se para acessar todos os recursos</p>
        </header>
        <form
          className="flex flex-col gap-5 px-5 w-full"
          onSubmit={handleSubmit}
        >
          <div className="flex gap-5">
            <div
              tabIndex={-1}
              className="relative avatar avatar-placeholder justify-center overflow-visible w-20 h-20 rounded-full focus-within:ring-2 outline-none ring-primary ring-offset-2 ring-offset-base-200"
            >
              <div className="bg-neutral text-neutral-content w-20 h-20 rounded-full">
                {data.avatar ? (
                  <img src={data.avatar.url} />
                ) : (
                  <span className="text-3xl">
                    {data.name.length > 0 ? data.name[0].toUpperCase() : "M"}
                  </span>
                )}
              </div>
              <ImageInput
                className="absolute bottom-0 right-0 btn btn-xs btn-circle btn-neutral focus:btn-primary"
                imageSize={{
                  aspect: 1,
                  height: 300,
                  width: 300,
                }}
                file={{
                  url: data.avatar?.url ?? "",
                  name: data.avatar?.filename,
                }}
                canClear={!!data.avatar}
                onImageClear={onClearAvatar}
                onImageLoaded={onLoadAvatar}
              >
                <FaUpload />
              </ImageInput>
            </div>
            <Input
              label="Nome"
              id="name"
              type="text"
              value={data.name}
              onChange={onChangeData}
              placeholder="Marcela"
            />
          </div>
          <Input
            label="E-mail"
            id="email"
            type="email"
            value={data.email}
            onChange={onChangeData}
            placeholder="marcela@email.com"
          />
          <Input
            label="Senha"
            id="password"
            type="password"
            value={data.password}
            onChange={onChangeData}
            placeholder="******"
          />
          <Input
            label="Confirmar senha"
            id="passwordConfirmation"
            type="passwordConfirmation"
            value={data.passwordConfirmation}
            onChange={onChangeData}
            placeholder="******"
          />
          <button
            className="btn btn-primary"
            type="submit"
            disabled={isLoading}
          >
            {isLoading && <span className="loading loading-spinner" />}
            {isLoading ? "Criando..." : "Criar"}
          </button>
        </form>
        <footer className="text-center">
          <p>
            Já tem uma conta?{" "}
            <Link to="/login" className="text-primary">
              Conecte-se
            </Link>
          </p>
        </footer>
      </section>
    </main>
  );
}
