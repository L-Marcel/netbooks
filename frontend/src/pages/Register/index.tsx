import { ChangeEvent, useState } from "react";
import Field from "@components/Input/Field";
import { Link, useNavigate } from "react-router-dom";
import { registerUser, UserRegisterData } from "../../services/user";
import ImageInput from "@components/Input/FileInput";
import { FaEnvelope, FaKey, FaUpload, FaUser } from "react-icons/fa";
import { ApiError, ValidationError } from "../../services/axios";
import AuthGuard from "@components/Guards/AuthGuard";
import Loading from "@components/Loading";
import { useLoading } from "@stores/useLoading";
import Button from "@components/Button";

export default function Register() {
  return (
    <AuthGuard onlyUnauthenticated>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const navigate = useNavigate();
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const [validations, setValidations] = useState<ValidationError>({});

  const [data, setData] = useState<UserRegisterData>({
    name: "",
    email: "",
    password: "",
    passwordConfirmation: "",
  });

  const onChangeData = (e: ChangeEvent<HTMLInputElement>) =>
    setData((data) => ({
      ...data,
      [e.target.name]: e.target.value,
    }));

  const onClearAvatar = () =>
    setData((data) => ({
      ...data,
      avatar: undefined,
    }));

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

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    startLoading("register");
    registerUser(data)
      .then(() => {
        navigate("/login");
      })
      .catch((error: ApiError) => {
        const { type, status, ...rest } = error;

        if (type === "validation" && status === 400) {
          setValidations({
            ...rest,
          } as ValidationError);
        }
      })
      .finally(() => {
        stopLoading("register");
      });
  };

  return (
    <main className="py-10 flex flex-col w-full h-full min-h-screen justify-center items-center bg-gradient-to-br from-base-100 via-base-200 to-base-300">
      <section className="flex flex-col gap-6 w-full max-w-11/12 sm:max-w-sm">
        <header className="text-center text-base-content">
          <h1 className="text-3xl font-bold text-base-content">Criar conta</h1>
          <p>Cadastre-se para acessar todos os recursos</p>
        </header>
        <form className="flex flex-col gap-5 px-5 w-full" onSubmit={onSubmit}>
          <div className="flex gap-5">
            <div
              tabIndex={-1}
              className="relative avatar avatar-placeholder justify-center overflow-visible size-20 rounded-full focus-within:ring-2 outline-none ring-primary ring-offset-2 ring-offset-base-200"
            >
              <div className="bg-neutral text-neutral-content size-20 rounded-full">
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
            <Field
              icon={FaUser}
              validations={validations["name"]}
              label="Nome"
              id="name"
              type="text"
              value={data.name}
              onChange={onChangeData}
              placeholder="Marcela"
            />
          </div>
          <Field
            icon={FaEnvelope}
            validations={validations["email"]}
            label="E-mail"
            id="email"
            type="email"
            value={data.email}
            onChange={onChangeData}
            placeholder="marcela@email.com"
          />
          <Field
            icon={FaKey}
            validations={validations["password"]}
            label="Senha"
            id="password"
            type="password"
            value={data.password}
            onChange={onChangeData}
            placeholder="••••••••"
          />
          <Field
            icon={FaKey}
            validations={validations["passwordConfirmation"]}
            label="Confirmar senha"
            id="passwordConfirmation"
            type="password"
            value={data.passwordConfirmation}
            onChange={onChangeData}
            placeholder="••••••••"
          />
          <Button className="btn btn-primary" type="submit">
            <Loading
              id="register"
              loadingMessage="Registrando..."
              defaultMessage="Registrar"
            />
          </Button>
        </form>
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
