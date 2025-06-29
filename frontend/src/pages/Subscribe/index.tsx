import PlanCard from "@components/Plan/PlanCard";
import { Plan } from "@models/plan";
import { Subscription } from "@models/subscription";
import { fetchAvailablePlans } from "@services/plans";
import {
  fetchSubscription,
  fetchNextSubscription,
  closeNextSubscriptions,
  subscribe,
} from "@services/subscriptions";
import { useCallback, useEffect, useMemo, useState } from "react";
import useLoading from "../../hooks/useLoading";

export default function Subscribe() {
  const loading = useLoading();
  const { clear: clearLoading } = loading;

  const [subscription, setSubscription] = useState<Subscription | undefined>(
    undefined
  );

  const [nextSubscription, setNextSubscription] = useState<
    Subscription | undefined
  >(undefined);

  const [plans, setPlans] = useState<Plan[]>([]);

  const update = useCallback(() => {
    Promise.all([
      fetchSubscription(),
      fetchNextSubscription().catch(() => undefined),
      fetchAvailablePlans(),
    ])
      .then(([subscription, nextSubscription, plans]) => {
        setSubscription(subscription);
        setNextSubscription(nextSubscription);
        setPlans(plans.sort((a, b) => a.benefits.length - b.benefits.length));
      })
      .catch(() => {
        setSubscription(undefined);
        setNextSubscription(undefined);
        setPlans([]);
      })
      .finally(clearLoading);
  }, [setSubscription, setNextSubscription, setPlans, clearLoading]);

  useEffect(update, [update]);

  const { mostPopular, mostEconomic } = useMemo(() => {
    return plans.reduce(
      (prev, curr) => {
        let mostPopular = prev?.mostPopular ?? curr;
        let mostEconomic = prev?.mostEconomic ?? curr;

        if (mostPopular && mostPopular?.numSubscribers <= curr.numSubscribers)
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
    <main className="p-8 gap-8 bg-base-100">
      <section className="columns-1 lg:columns-2 xl:columns-3 w-full">
        {plans.map((plan) => (
          <PlanCard
            key={plan.id}
            plan={plan}
            loading={loading}
            subscription={subscription}
            nextSubscription={nextSubscription}
            mostPopular={mostPopular && mostPopular?.id === plan.id}
            mostEconomic={mostEconomic && mostEconomic?.id === plan.id}
            onCancelNextSubscriptions={() => {
              loading.start(plan.id);
              closeNextSubscriptions().finally(update);
            }}
            onSubscribe={(edition) => {
              loading.start(plan.id);
              subscribe(edition.id).finally(update);
            }}
          />
        ))}
      </section>
    </main>
  );
}
