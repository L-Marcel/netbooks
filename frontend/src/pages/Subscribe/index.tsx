import PlanCard from "@components/PlanCard";
import { Plan } from "@models/plan";
import { fetchAvailablePlans } from "@services/plans";
import { useEffect, useState } from "react";

export default function Subscribe() {
  const [plans, setPlans] = useState<Plan[]>([]);

  useEffect(() => {
    fetchAvailablePlans().then((plans) => {
      setPlans(plans);
    });
  }, []);

  const mostPopular = plans.reduce(
    (prev, curr) => {
      if (prev && prev?.popularity > curr.popularity) return prev;
      return curr;
    },
    undefined as Plan | undefined
  );

  return (
    <main className="flex flex-row w-full p-8 gap-8 justify-center items-center bg-base-100">
      {plans.map((plan) => (
        <PlanCard
          key={plan.id}
          plan={plan}
          mostPopular={mostPopular && mostPopular?.id === plan.id}
        />
      ))}
    </main>
  );
}
