import PlanCard from "@components/Plan";
import { Plan } from "@models/plan";
import { fetchAvailablePlans } from "@services/plans";
import { useEffect, useState } from "react";

export default function Subscribe() {
  const [plans, setPlans] = useState<Plan[]>([]);

  useEffect(() => {
    fetchAvailablePlans().then((plans) => {
      setPlans(plans.sort((a, b) => a.benefits.length - b.benefits.length));
    });
  }, []);

  const { mostPopular, mostEconomic } = plans.reduce(
    (prev, curr) => {
      let mostPopular = prev?.mostPopular ?? curr; 
      let mostEconomic = prev?.mostEconomic ?? curr;

      if (mostPopular && mostPopular?.numSubscribers >= curr.numSubscribers) mostPopular = curr;
      if (mostEconomic && mostEconomic?.getScore().lessThanOrEqualTo(curr.getScore())) mostEconomic = curr;
      
      return {
        mostPopular,
        mostEconomic,
      };
    },
    {} as {
      mostPopular?: Plan;
      mostEconomic?: Plan;
    }
  );

  return (
    <main className="flex flex-row flex-wrap w-full p-8 gap-8 justify-center bg-base-100">
      {plans.map((plan) => (
        <PlanCard
          key={plan.id}
          plan={plan}
          mostPopular={mostPopular && mostPopular?.id === plan.id}
          mostEconomic={mostEconomic && mostEconomic?.id === plan.id}
        />
      ))}
    </main>
  );
}
