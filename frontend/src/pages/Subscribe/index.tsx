import PlanCard from "@components/Plan/PlanCard";
import { Plan } from "@models/plan";
import { Subscription } from "@models/subscription";
import { fetchAvailablePlans } from "@services/plans";
import { fetchSubscription, renewSubscription, subscribe, unsubscribe } from "@services/subscriptions";
import { useCallback, useEffect, useMemo, useState } from "react";

export default function Subscribe() {
  const [subscription, setSubscription] = useState<
    Subscription | undefined
  >(undefined);
  const [plans, setPlans] = useState<Plan[]>([]);

  const update = useCallback(() => {
    fetchSubscription().then((subscription) => {
      setSubscription(subscription);
    });
    fetchAvailablePlans().then((plans) => {
      setPlans(plans.sort((a, b) => a.benefits.length - b.benefits.length));
    });
  }, [setSubscription, setPlans]);

  useEffect(() => {
    update();
  }, [update]);

  const { mostPopular, mostEconomic } = useMemo(() => {
    return plans.reduce(
      (prev, curr) => {
        let mostPopular = prev?.mostPopular ?? curr;
        let mostEconomic = prev?.mostEconomic ?? curr;

        if (
          mostPopular && 
          mostPopular?.numSubscribers <= curr.numSubscribers
        ) mostPopular = curr;

        if (
          mostEconomic &&
          mostEconomic?.getScore().lessThanOrEqualTo(curr.getScore())
        ) mostEconomic = curr;

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
          subscription={subscription}
          mostPopular={mostPopular && mostPopular?.id === plan.id}
          mostEconomic={mostEconomic && mostEconomic?.id === plan.id}
          onSubscribe={(edition) => subscribe(edition.id).finally(() => update())}
          onUnsubscribe={() => unsubscribe().finally(() => update())}
          onRenew={() => renewSubscription().finally(() => update())}
        />
      ))}
    </main>
  );
}
