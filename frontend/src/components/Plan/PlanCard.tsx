import { Plan } from "@models/plan";
import {
  FaBullhorn,
  FaFireAlt,
  FaPiggyBank,
  FaRegCalendarCheck,
  FaRegCalendarTimes,
} from "react-icons/fa";
import PlanTags, { PlanTag } from "./PlanTags";
import { format } from "date-fns";
import Decimal from "decimal.js";
import { Subscription } from "@models/subscription";
import { PlanEdition } from "@models/plan_edition";

interface Props {
  plan: Plan;
  isLoading: boolean;
  disabled: boolean;
  subscription?: Subscription;
  nextSubscription?: Subscription;
  mostPopular?: boolean;
  mostEconomic?: boolean;
  onSubscribe: (edition: PlanEdition) => void;
  onCancelNextSubscriptions: () => void;
}

export default function PlanCard({
  plan,
  subscription,
  nextSubscription,
  mostPopular,
  mostEconomic,
  isLoading,
  disabled,
  onSubscribe,
  onCancelNextSubscriptions,
}: Props) {
  const isUserPlan = subscription && plan.id === subscription?.edition.plan;
  const isNextUserPlan =
    !isUserPlan &&
    nextSubscription &&
    plan.id === nextSubscription?.edition.plan;

  const currentEdition = isUserPlan
    ? subscription.edition
    : isNextUserPlan
      ? nextSubscription.edition
      : plan.getCheapestEdition();

  const currentPrice = currentEdition?.price ?? new Decimal(1);

  const promotional: boolean = !currentPrice.equals(
    plan.getMostExpensivePrice()
  );

  const tags: PlanTag[] = [];

  if (mostPopular)
    tags.push({
      value: "Popular",
      style: "badge-warning pl-2",
      icon: FaFireAlt,
    });
  if (promotional)
    tags.push({
      value: "Promoção",
      style: "badge-primary pl-2",
      icon: FaBullhorn,
    });
  if (mostEconomic)
    tags.push({
      value: "Econômico",
      style: "badge-primary pl-2",
      icon: FaPiggyBank,
    });

  const endDate = plan.getCheapestEndDate();

  return (
    <div className="card w-full bg-base-200 shadow-sm h-min break-inside-avoid mb-8">
      <div className="card-body relative">
        <PlanTags tags={tags} />
        <div className="flex flex-row justify-between items-center">
          <h2 className="text-3xl font-bold">{plan.name}</h2>
          <div className="flex flex-col flex-nowrap items-end">
            {promotional && (
              <p className="text-xs h-min flex line-through">
                R$ {plan.getMostExpensivePrice().toFixed(2)} /{" "}
                {plan.getDurationText()}
              </p>
            )}
            <p className="text-xl h-min flex">
              R$ {currentPrice.toFixed(2)} / {plan.getDurationText()}
            </p>
          </div>
        </div>
        <p className="grow-0">{plan.description}</p>
        <ul className="mt-2 flex flex-col grow h-min gap-2 text-xs">
          {plan.getPlanBenefits().map((benefit) => (
            <li
              key={benefit.value}
              className={benefit.actived ? "" : "opacity-50"}
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                className={`size-4 me-2 inline-block ${benefit.actived ? "text-success" : "text-base-content/50"}`}
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth="2"
                  d="M5 13l4 4L19 7"
                />
              </svg>
              <span className={benefit.actived ? "" : "line-through"}>
                {benefit.description}
              </span>
            </li>
          ))}
        </ul>
        <div className="mt-4">
          {promotional && (
            <p className="ml-0.5 mb-4 text-xs font-extralight italic">
              Promoção disponível{" "}
              {endDate
                ? "até " + format(endDate, "dd/MM/yyyy")
                : "por tempo indeterminado"}
            </p>
          )}
          {isNextUserPlan ? (
            <button
              onClick={onCancelNextSubscriptions}
              type="button"
              className="btn btn-error btn-block"
              disabled={isUserPlan || disabled || isLoading}
            >
              {isLoading && !disabled && (
                <span className="loading loading-spinner" />
              )}
              {isLoading ? "Interrompendo mudança..." : "Interromper Mudança"}
            </button>
          ) : (
            <button
              onClick={() =>
                onSubscribe(plan.getCheapestEdition() as PlanEdition)
              }
              type="button"
              className="btn btn-primary btn-block"
              disabled={isUserPlan || disabled || isLoading}
            >
              {isLoading && !disabled && (
                <span className="loading loading-spinner" />
              )}
              {isUserPlan
                ? "Inscrito"
                : isLoading
                  ? "Inscrevendo-se..."
                  : "Inscreve-se"}
            </button>
          )}
          {isUserPlan && subscription?.closedIn && (
            <p className="ml-0.5 mt-4 text-xs flex flex-row items-center gap-2 font-light">
              <FaRegCalendarTimes className="size-5" /> Cancelamento programando
              para {format(subscription?.closedIn, "dd/MM/yyyy")}
            </p>
          )}
          {isNextUserPlan && (
            <p className="ml-0.5 mt-4 text-xs flex flex-row items-center gap-2 font-light">
              <FaRegCalendarCheck className="size-5" /> Início programando para{" "}
              {format(nextSubscription.startedIn, "dd/MM/yyyy")}
            </p>
          )}
        </div>
      </div>
    </div>
  );
}
