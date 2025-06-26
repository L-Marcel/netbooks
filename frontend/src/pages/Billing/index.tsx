import AuthGuard from "@components/Guards/AuthGuard";
import PaymentCard from "@components/Payment/PaymentCard";
import { Payment } from "@models/payment";
import { fetchPayments } from "@services/payment";
import { useCallback, useEffect, useState } from "react";

export default function Billing() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  const update = useCallback(() => {
    fetchPayments()
      .then((payments) => setPayments(payments))
      .catch(() => setPayments([]))
      .finally(() => setIsLoading(false));
  }, []);

  useEffect(() => {
    update();
  }, [update]);

  return (
    <main className="p-8 gap-8 bg-base-100">
      <section className="w-full">
        <ul className="list w-full rounded-box bg-base-200 shadow-sm">
          {payments.map((payment) => (
            <PaymentCard key={payment.id} payment={payment} />
          ))}
        </ul>
      </section>
    </main>
  );
}
