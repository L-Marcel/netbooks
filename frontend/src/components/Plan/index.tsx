import { Plan } from "@models/plan";
import { FaBullhorn, FaFireAlt, FaPiggyBank } from "react-icons/fa";
import PlanTags, { PlanTag } from "./PlanTags";

interface Props {
  plan: Plan;
  mostPopular?: boolean;
  mostEconomic?: boolean;
}

export default function PlanCard({ plan, mostPopular, mostEconomic }: Props) {
  const promotional: boolean = !plan
    .getCheapestPrice()
    .equals(plan.getMostExpensivePrice());
  const tags: PlanTag[] = [];

  if (mostPopular) tags.push({ value: "Popular", style: "badge-warning pl-2", icon: FaFireAlt });
  if (promotional) tags.push({ value: "Promoção", style: "badge-primary pl-2", icon: FaBullhorn });
  if (mostEconomic) tags.push({ value: "Econômico", style: "badge-primary pl-2", icon: FaPiggyBank });

  return (
    <div className="card w-96 bg-base-200 shadow-sm">
      <div className="card-body relative">
        <PlanTags tags={tags} />
        <div className="flex flex-row justify-between items-center">
          <h2 className="text-3xl font-bold">{plan.name}</h2>
          <div className="flex flex-col flex-nowrap items-end">
            {promotional && (
              <p className="text-xs h-min flex line-through">
                R$ {plan.getMostExpensivePrice().toFixed(2)} / {plan.getDurationText()}
              </p>
            )}
            <p className="text-xl h-min flex">
              R$ {plan.getCheapestPrice().toFixed(2)} / {plan.getDurationText()}
            </p>
          </div>
        </div>
        <p>{plan.description}</p>
        <ul className="mt-2 flex flex-col gap-2 text-xs">
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
        <div className="mt-6">
          <button type="button" className="btn btn-primary btn-block">
            Inscreve-se
          </button>
        </div>
      </div>
    </div>
  );
}
