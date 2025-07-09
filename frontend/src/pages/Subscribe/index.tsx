import PlanCard from "@components/Plan/PlanCard";
import { Plan } from "@models/plan";
import { Subscription } from "@models/subscription";
import { fetchAvailablePlans } from "@services/plans";
import {
  fetchNextSubscription,
  closeNextSubscriptions,
  subscribe,
} from "@services/subscriptions";
import { fetchUpdatedUser } from "@services/user";
import { useLoading } from "@stores/useLoading";
import useUser from "@stores/useUser";
import { useCallback, useEffect, useMemo, useState } from "react";
import { useNavigate } from "react-router-dom";
import { motion } from "motion/react";

export default function Subscribe() {
  const user = useUser((state) => state.user);
  const startLoading = useLoading((state) => state.start);
  const clearLoading = useLoading((state) => state.clear);
  const hasLoading = useLoading((state) => state.has);
  const navigate = useNavigate();

  const [nextSubscription, setNextSubscription] = useState<
    Subscription | undefined
  >(undefined);

  const [plans, setPlans] = useState<Plan[]>([]);

  const update = useCallback(() => {
    startLoading("plans");
    Promise.all([
      fetchNextSubscription().catch(() => undefined),
      fetchAvailablePlans(),
    ])
      .then(async ([nextSubscription, plans]) => {
        await fetchUpdatedUser().catch(() => {});
        setNextSubscription(nextSubscription);
        setPlans(plans.sort((a, b) => a.benefits.length - b.benefits.length));
      })
      .catch(() => {
        setNextSubscription(undefined);
        setPlans([]);
      })
      .finally(clearLoading);
  }, [setNextSubscription, setPlans, startLoading, clearLoading]);

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
        {hasLoading("plans") || plans.length == 0 ? (
          <>
            <motion.div className="skeleton card w-full bg-base-200 shadow-sm h-100 break-inside-avoid mb-8" />
            <motion.div className="skeleton card w-full bg-base-200 shadow-sm h-100 break-inside-avoid mb-8" />
            <motion.div className="skeleton card w-full bg-base-200 shadow-sm h-100 break-inside-avoid mb-8" />
          </>
        ) : (
          plans.map((plan, index) => (
            <PlanCard
              index={index}
              key={plan.id}
              plan={plan}
              subscription={user?.subscription}
              nextSubscription={nextSubscription}
              mostPopular={mostPopular && mostPopular?.id === plan.id}
              mostEconomic={mostEconomic && mostEconomic?.id === plan.id}
              onCancelNextSubscriptions={() => {
                startLoading(plan.id);
                if (user) closeNextSubscriptions().finally(update);
                else navigate("/login");
              }}
              onSubscribe={(edition) => {
                startLoading(plan.id);
                if (user) subscribe(edition.id).finally(update);
                else navigate("/login");
              }}
            />
          ))
        )}
      </section>
    </main>
  );
}
