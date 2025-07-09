import { create } from "zustand";

export type LoadingStore = {
  loadingSet: Set<string | number>;
  hasAny: boolean;
  has: (id: string | number) => boolean;
  start: (id: string | number) => void;
  stop: (id: string | number) => void;
  clear: () => void;
};

export const useLoading = create<LoadingStore>((set, get) => ({
  loadingSet: new Set<string | number>(),
  hasAny: false,
  clear: () => set({ loadingSet: new Set<string | number>(), hasAny: false }),
  has: (id) => get().loadingSet.has(id),
  start: (id) => {
    const newSet = new Set<string | number>(get().loadingSet);
    newSet.add(id);
    set({ loadingSet: newSet, hasAny: true });
  },
  stop: (id) => {
    const newSet = new Set<string | number>(get().loadingSet);
    newSet.delete(id);
    set({ loadingSet: newSet, hasAny: newSet.size > 0 });
  },
}));
