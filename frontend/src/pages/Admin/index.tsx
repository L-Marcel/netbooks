import BookForm from "@components/Book/BookForm";
import AdminGuard from "@components/Guards/AdminGuard";
import { useState } from "react";

const tabs = {
  Livros: <BookForm />,
  Edições: <></>,
};

export default function Admin() {
  return (
    <AdminGuard>
      <Page />
    </AdminGuard>
  );
}

export function Page() {
  const [tab, setTab] = useState<keyof typeof tabs>("Livros");

  const onChangeTab = (e: React.ChangeEvent<HTMLInputElement>) =>
    setTab(e.target.name as keyof typeof tabs);

  return (
    <main className="flex flex-col p-8 gap-4 bg-base-100">
      <section className="flex flex-col gap-8 w-full">
        <div className="tabs tabs-box gap-2 p-4 w-full bg-base-200 overflow-hidden rounded-box shadow-sm">
          <input
            type="radio"
            name="Livros"
            className="tab rounded-box grow !bg-base-300 checked:!bg-primary text-primary-content"
            aria-label="Livros"
            onChange={onChangeTab}
            checked={tab === "Livros"}
          />
          <input
            type="radio"
            name="Edições"
            className="tab rounded-box grow !bg-base-300 checked:!bg-primary text-primary-content"
            aria-label="Edições"
            onChange={onChangeTab}
            checked={tab === "Edições"}
          />
        </div>
      </section>
      {tabs[tab]}
    </main>
  );
}
