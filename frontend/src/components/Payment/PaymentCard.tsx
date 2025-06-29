import { Payment } from "@models/payment";
import { format } from "date-fns";
import {
  FaRegCheckCircle,
  FaRegCircle,
  FaRegTimesCircle,
} from "react-icons/fa";
import { LoadingContext } from "../../hooks/useLoading";
import Loading from "@components/Loading";

interface Props {
  payment: Payment;
  loading: LoadingContext;
  onPay: () => void;
}

export default function PaymentCard({ loading, payment, onPay }: Props) {
  const canPay = payment.status === "SCHEDULED" && !payment.isOverdue();

  let icon = <FaRegTimesCircle className="size-6 text-error" />;
  let border = <span className="w-2 h-auto bg-error" />;
  let badge = (
    <span className="badge text-md font-semibold badge-error">
      Pagamento vencido
    </span>
  );

  if (!payment.isOverdue()) {
    switch (payment.status) {
      case "SCHEDULED":
        icon = <FaRegCircle className="size-6 text-info" />;
        border = <span className="w-2 h-auto bg-info" />;
        badge = (
          <span className="badge text-md font-semibold badge-info">
            Pagamento pendente
          </span>
        );
        break;
      case "PAID":
        icon = <FaRegCheckCircle className="size-6 text-success" />;
        border = <span className="w-2 h-auto bg-success" />;
        badge = (
          <span className="badge text-md font-semibold badge-success">
            Pagamento efetuado
          </span>
        );
        break;
      case "CANCELED":
        badge = (
          <span className="badge text-md font-semibold badge-error">
            Pagamento cancelado
          </span>
        );
        break;
    }
  }

  return (
    <li className="flex flex-row w-full bg-base-200 overflow-hidden rounded-box shadow-sm">
      {border}
      <div className="flex flex-col w-full">
        <div className="flex flex-col p-4 bg-base-300">
          <div className="flex flex-row flex-nowrap items-center gap-2">
            {icon} {badge}
          </div>
          <div className="divider divider-vertical my-1"></div>
          <p>
            Plano {payment.product.name.toLowerCase()} por{" "}
            {payment.product.getDurationText()}
          </p>
          <h2 className="font-bold text-xl flex">
            R$ {payment.price.toFixed(2)}
          </h2>
        </div>
        <div className="flex flex-col p-4">
          <h3 className="text-md font-semibold mb-1">Informações extras</h3>
          <p className="text-sm font-extralight">
            Emissão: {format(payment.payDate, "dd/MM/yyyy")}
          </p>
          <p className="text-sm font-extralight">
            Vencimento: {format(payment.dueDate, "dd/MM/yyyy")}
          </p>
          {payment.status === "PAID" && (
            <p className="text-sm font-extralight">
              Confirmação: {format(payment.paidAt, "dd/MM/yyyy")}
            </p>
          )}
          {canPay && (
            <button
              disabled={loading.hasAny}
              type="button"
              onClick={onPay}
              className="mt-4 btn btn-info btn-md w-min text-nowrap"
            >
              <Loading
                isLoading={loading.has(payment.id)}
                loadingMessage="Realizando pagamento..."
                defaultMessage="Realizar pagamento"
              />
            </button>
          )}
        </div>
      </div>
    </li>
  );
}
