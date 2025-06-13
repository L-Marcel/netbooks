import { IconType } from "react-icons";

export type PlanTag = {
  value: string;
  style: string;
  icon?: IconType;
};

interface Props {
  tags?: PlanTag[];
}

const DEFAULT_TAGS: PlanTag[] = [];

export default function PlanTags({ tags = DEFAULT_TAGS }: Props) {
  const MAX_VISIBLE_TAGS = 2;
  const visibleTags =
    tags.length > MAX_VISIBLE_TAGS ? tags.slice(0, MAX_VISIBLE_TAGS) : tags;
  const extraTagsCount = tags.length - MAX_VISIBLE_TAGS;

  return (
    <ul className="absolute top-0 -translate-y-1/2 flex flex-row gap-2">
      {visibleTags.map(({ value, style, icon: Icon }) => (
        <li key={value}>
          <span className={`badge badge-lg ${style}`}>
            {Icon && <Icon />}
            {value}
          </span>
        </li>
      ))}
      {extraTagsCount > 0 && (
        <li
          className="badge badge-lg px-2 badge-secondary tooltip tooltip-secondary tooltip-bottom"
          data-tip={tags
            .map((tag) => tag.value)
            .slice(MAX_VISIBLE_TAGS)
            .join(", ")}
        >
          +{extraTagsCount}
        </li>
      )}
    </ul>
  );
}
