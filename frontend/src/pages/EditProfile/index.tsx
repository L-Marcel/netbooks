import AuthGuard from "@components/Guards/AuthGuard";
import UserForm from "@components/User/UserForm";
import useUser from "@stores/useUser";

export default function EditProfile() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const user = useUser((state) => state.user);

  return (
    <main className="py-10 flex flex-col w-full h-full min-h-[calc(100vh-4rem-2px)] justify-center items-center bg-gradient-to-br from-base-100 via-base-200 to-base-300">
      <section className="flex flex-col gap-6 w-full max-w-11/12 sm:max-w-sm">
        <header className="text-center text-base-content">
          <h1 className="text-3xl font-bold text-base-content">
            Editar perfil
          </h1>
          <p>Aqui você poder atualizar seus perfil</p>
        </header>
        <UserForm user={user} />
      </section>
    </main>
  );
}
