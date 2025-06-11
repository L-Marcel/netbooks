import { Plan } from "@models/plan";

interface Props {
  plan: Plan;
  mostPopular?: boolean;
}

export default function PlanCard({ plan, mostPopular }: Props) {
  return (
    <div className="card w-96 bg-base-200 shadow-sm">
      <div className="card-body">
        {mostPopular && <span className="badge badge-xs badge-warning">Most Popular</span>}
        <div className="flex justify-between">
          <h2 className="text-3xl font-bold">{plan.name}</h2>
          <span className="text-xl">
            R$ {plan.getCurrentPrice().toFixed(2)}/mo
          </span>
        </div>
        <ul className="mt-6 flex flex-col gap-2 text-xs">
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
