import AuthGuard from "@components/Guards/AuthGuard";
import PaymentCard from "@components/Payment/PaymentCard";
import { Payment } from "@models/payment";
import { RenewDetails } from "@models/renew_details";
import { fetchPayments, pay } from "@services/payment";
import { fetchRenewDetails } from "@services/subscriptions";
import { switchAutomaticBilling } from "@services/user";
import useUser from "@stores/useUser";
import { formatDate } from "date-fns";
import { useCallback, useEffect, useState } from "react";
import { FaTriangleExclamation } from "react-icons/fa6";
import Loading from "@components/Loading";
import { useLoading } from "@stores/useLoading";
import Button from "@components/Button";
import Input from "@components/Input";

export default function Billing() {
  return (
    <AuthGuard>
      <Page />
    </AuthGuard>
  );
}

function Page() {
  const user = useUser((state) => state.user);
  const startLoading = useLoading((state) => state.start);
  const stopLoading = useLoading((state) => state.stop);

  const [payments, setPayments] = useState<Payment[]>([]);
  const [renewDetails, setRenewDetails] = useState<RenewDetails>();

  const update = useCallback(() => {
    startLoading("payments");
    fetchPayments()
      .then((payments) => setPayments(payments))
      .catch(() => setPayments([]))
      .finally(() => stopLoading("payments"));
    fetchRenewDetails()
      .then((renewDetails) => setRenewDetails(renewDetails))
      .catch(() => setRenewDetails(undefined))
      .finally(() => stopLoading("renew_details"));
    stopLoading("automatic_billing");
  }, [startLoading, stopLoading]);

  const onSwitchAutomaticBilling = useCallback(() => {
    startLoading("automatic_billing");
    switchAutomaticBilling().finally(update);
  }, [update, startLoading]);

  const onPay = useCallback(
    (id: number) => {
      startLoading(id);
      pay().finally(() => {
        update();
        stopLoading(id);
      });
    },
    [update, stopLoading, startLoading]
  );

  useEffect(update, [update]);

  return (
    <main className="p-8 gap-8 bg-base-100">
      <section className="flex flex-col gap-8 w-full">
        {renewDetails && renewDetails?.needManualRenew() && (
          <div role="alert" className="alert alert-warning alert-soft -mb-4">
            <FaTriangleExclamation className="size-6" />
            <span className="-ml-1">
              Sua assinatura vai acabar em{" "}
              {formatDate(renewDetails?.dueDate, "dd/MM/yyyy")}!
            </span>
            <div>
              <Button onClick={() => onPay(-1)} className="btn btn-warning">
                <Loading
                  id={-1}
                  loadingMessage="Renovando assinatura..."
                  defaultMessage="Renovar assinatura"
                />
              </Button>
            </div>
          </div>
        )}
        <div className="flex flex-col gap-2 w-full p-6 bg-base-200 overflow-hidden rounded-box shadow-sm">
          <h1 className="text-lg font-bold">Renovação automática</h1>
          <p>
            Quando ativado as cobranças futuras serão feitas automaticamente
            assim que a assinatura estiver prestes a acabar. Não se aplica a
            assinaturas que já estão prestes a se encerrar, essas precisam ser
            renovadas manualmente.
          </p>
          <label className="label mt-1">
            <Input
              type="checkbox"
              onChange={onSwitchAutomaticBilling}
              checked={user?.automaticBilling}
              className="toggle text-base-content"
            />
            <Loading
              id="automatic_billing"
              loadingMessage={
                user?.automaticBilling ? "Desativando..." : "Ativando..."
              }
              defaultMessage={user?.automaticBilling ? "Ativado" : "Desativado"}
            />
          </label>
        </div>
        <ul className="flex flex-col gap-8">
          {payments.map((payment) => (
            <PaymentCard
              onPay={() => onPay(payment.id)}
              key={payment.id}
              payment={payment}
            />
          ))}
        </ul>
      </section>
    </main>
  );
}
