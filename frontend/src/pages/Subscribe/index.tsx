import PlanCard from "@components/Plan/PlanCard";
import { Plan } from "@models/plan";
import { PlanEdition } from "@models/plan_edition";
import { fetchAvailablePlans } from "@services/plans";
import { fetchUserPlanEdition } from "@services/plans_editions";
import { useEffect, useMemo, useState } from "react";

export default function Subscribe() {
  const [userPlanEdition, setUserPlanEdition] = useState<PlanEdition | undefined>(undefined);
  const [plans, setPlans] = useState<Plan[]>([]);

  useEffect(() => {
    fetchUserPlanEdition().then((userPlanEdition) => {
      setUserPlanEdition(userPlanEdition);
    });
    fetchAvailablePlans().then((plans) => {
      setPlans(plans.sort(
        (a, b) => a.benefits.length - b.benefits.length
      ));
    });
  }, []);

  const { mostPopular, mostEconomic } = useMemo(() => {
    return plans.reduce(
      (prev, curr) => {
        let mostPopular = prev?.mostPopular ?? curr;
        let mostEconomic = prev?.mostEconomic ?? curr;

        if (mostPopular && mostPopular?.numSubscribers >= curr.numSubscribers)
          mostPopular = curr;
        if (
          mostEconomic &&
          mostEconomic?.getScore().lessThanOrEqualTo(curr.getScore())
        )
          mostEconomic = curr;

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
  }, [plans]);

  return (
    <main className="columns-1 lg:columns-2 xl:columns-3 w-full p-8 gap-8 bg-base-100">
      {plans.map((plan) => (
        <PlanCard
          key={plan.id}
          plan={plan}
          userPlanEdition={userPlanEdition}
          mostPopular={mostPopular && mostPopular?.id === plan.id}
          mostEconomic={mostEconomic && mostEconomic?.id === plan.id}
        />
      ))}
    </main>
  );
}
