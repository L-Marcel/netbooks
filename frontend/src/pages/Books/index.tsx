import AuthGuard from "@components/Guards/AuthGuard";
import SubscriberGuard from "@components/Guards/SubscriberGuard";

export default function Books() {
  return (
    <AuthGuard>
      <SubscriberGuard>
        <Page />
      </SubscriberGuard>
    </AuthGuard>
  );
}

function Page() {
  return (
    <main className="flex flex-col w-full h-ful items-center bg-base-100"></main>
  );
}
