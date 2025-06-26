import { Payment } from "@models/payment";
import { paymentStatusToIcon } from "@models/payment_status";

interface Props {
  payment: Payment;
}

export default function PaymentCard({ payment }: Props) {
  return (
    <li className="list-row w-full">
      <div className="flex flex-col justify-center">
        {paymentStatusToIcon[payment.status]}
      </div>
      <div>
        <h1 className="font-bold text-xl">R$ {payment.price.toFixed(2)}</h1>
      </div>
    </li>
  );
}
