import { ChangeEvent, useState } from "react";
import Field from "@components/Input/Field";
import { useNavigate } from "react-router-dom";
import {
  fetchUpdatedUser,
  registerUser,
  updateUser,
  UserRegisterData,
  UserUpdateData,
} from "../../services/user";
import ImageInput from "@components/Input/ImageInput";
import { FaEnvelope, FaKey, FaUpload, FaUser } from "react-icons/fa";
import { ApiError, ValidationErrors } from "../../services/axios";
import Loading from "@components/Loading";
import { useLoading } from "@stores/useLoading";
import Button from "@components/Button";
import { User } from "@models/user";
import Input from "@components/Input";

interface Props {
  user?: User;
}

export default function UserForm({ user }: Props) {
  const navigate = useNavigate();
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const [validations, setValidations] = useState<ValidationErrors>({});
  const [updatePassword, setUpdatePassword] = useState<boolean>(false);
  const [oldPassword, setOldPassword] = useState<string>("");
  const [data, setData] = useState<UserRegisterData>({
    name: user?.name ?? "",
    email: user?.email ?? "",
    avatar: user?.getAvatar() ?? undefined,
    password: "",
    passwordConfirmation: "",
  });

  const onChangeUpdatePassword = (e: ChangeEvent<HTMLInputElement>) =>
    setUpdatePassword(e.target.checked);

  const onChangeOldPassword = (e: ChangeEvent<HTMLInputElement>) =>
    setOldPassword(e.target.value);

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
        blob,
        base64,
        filename,
      },
    }));
  };

  const onCatchErrors = (error: ApiError) => {
    const { type, status, ...rest } = error;

    if (type === "validation" && status === 400) {
      setValidations({
        ...rest,
      } as ValidationErrors);
    }
  };

  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (user) startLoading("profile-edit");
    else startLoading("user-register");

    const formData = new FormData();

    let body = {
      name: data.name,
      email: data.email,
      password: data.password,
      passwordConfirmation: data.passwordConfirmation,
    } as UserRegisterData;

    if (user) {
      body = {
        ...body,
        updatePassword,
        oldPassword,
      } as UserUpdateData;
    }

    formData.append(
      "body",
      new Blob([JSON.stringify(body)], { type: "application/json" })
    );

    if (data.avatar?.blob) {
      formData.append("avatar", data.avatar.blob, data.avatar.filename);
    }

    if (user) {
      updateUser(formData)
        .then(async () => {
          await fetchUpdatedUser();
          navigate("/home");
        })
        .catch(onCatchErrors)
        .finally(() => {
          stopLoading("profile-edit");
        });
    } else {
      registerUser(formData)
        .then(() => {
          navigate("/login");
        })
        .catch(onCatchErrors)
        .finally(() => {
          stopLoading("user-register");
        });
    }
  };

  return (
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
      {user && (
        <>
          <Field
            icon={FaKey}
            validations={validations["oldPassword"]}
            label="Senha atual"
            id="oldPassword"
            type="password"
            value={oldPassword}
            onChange={onChangeOldPassword}
            placeholder="••••••••"
          />
          <label className="label">
            <Input
              type="checkbox"
              checked={updatePassword}
              onChange={onChangeUpdatePassword}
              className="checkbox checkbox-primary"
            />
            Alterar senha
          </label>
        </>
      )}
      {(!user || updatePassword) && (
        <>
          <Field
            icon={FaKey}
            validations={validations["password"]}
            label={user ? "Nova senha" : "Senha"}
            id="password"
            type="password"
            value={data.password}
            onChange={onChangeData}
            placeholder="••••••••"
          />
          <Field
            icon={FaKey}
            validations={validations["passwordConfirmation"]}
            label={user ? "Confirmar nova senha" : "Confirmar senha"}
            id="passwordConfirmation"
            type="password"
            value={data.passwordConfirmation}
            onChange={onChangeData}
            placeholder="••••••••"
          />
        </>
      )}
      <Button className="btn btn-primary" type="submit">
        {user ? (
          <Loading
            id="profile-edit"
            loadingMessage="Salvando..."
            defaultMessage="Salvar"
          />
        ) : (
          <Loading
            id="user-register"
            loadingMessage="Registrando..."
            defaultMessage="Registrar"
          />
        )}
      </Button>
    </form>
  );
}
