import AuthGuard from "@components/Guards/AuthGuard";
import PaymentCard from "@components/Payment/PaymentCard";
import { Payment } from "@models/payment";
import { fetchPayments, pay } from "@services/payment";
import { switchAutomaticBilling } from "@services/user";
import useUser from "@stores/useUser";
import { useCallback, useEffect, useState } from "react";

export default function Billing() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const user = useUser((state) => state.user);
  const [payments, setPayments] = useState<Payment[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const update = useCallback(() => {
    fetchPayments()
      .then((payments) => setPayments(payments))
      .catch(() => setPayments([]))
      .finally(() => setIsLoading(false));
  }, []);

  const onSwitchAutomaticBilling = useCallback(() => {
    setIsLoading(true);
    switchAutomaticBilling().finally(() => update());
  }, [update]);

  const onPay = useCallback(() => {
    setIsLoading(true);
    pay().finally(() => update());
  }, [update]);

  useEffect(() => {
    update();
  }, [update]);

  return (
    <main className="p-8 gap-8 bg-base-100">
      <section className="flex flex-col gap-8 w-full">
        <div className="flex flex-col gap-2 w-full p-6 bg-base-200 overflow-hidden rounded-box shadow-sm">
          <h1 className="text-lg font-bold">Renovação automática</h1>
          <label className="label">
            <input
              type="checkbox"
              disabled={isLoading}
              onChange={onSwitchAutomaticBilling}
              checked={user?.automaticBilling}
              className="toggle text-base-content"
            />
            {user?.automaticBilling ? "Ativado" : "Desativado"}
          </label>
        </div>
        <ul className="flex flex-col gap-8">
          {payments.map((payment) => (
            <PaymentCard
              onPay={onPay}
              isLoading={isLoading}
              key={payment.id}
              payment={payment}
            />
          ))}
        </ul>
      </section>
    </main>
  );
}
