import { FaRegCheckCircle, FaRegCircle, FaRegTimesCircle } from "react-icons/fa";

export enum PaymentStatus {
  SCHEDULED = "SCHEDULED",
  PAID = "PAID",
  CANCELED = "CANCELED",
}

export const paymentStatusToIcon = {
  [PaymentStatus.SCHEDULED]: <FaRegCircle className="size-6 text-info"/>,
  [PaymentStatus.PAID]: <FaRegCheckCircle className="size-6 text-success"/>,
  [PaymentStatus.CANCELED]: <FaRegTimesCircle className="size-6 text-error"/>,
};