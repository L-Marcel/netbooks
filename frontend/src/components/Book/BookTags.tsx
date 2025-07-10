import { Tag } from "@models/tag";

interface Props {
  tags?: Tag[];
  isPremium?: boolean;
}

const DEFAULT_TAGS: Tag[] = [];

export default function BookTag({ tags = DEFAULT_TAGS }: Props) {
  const MAX_VISIBLE_TAGS = 3;
  const visibleTags =
    tags.length > MAX_VISIBLE_TAGS ? tags.slice(0, MAX_VISIBLE_TAGS) : tags;
  const extraTagsCount = tags.length - MAX_VISIBLE_TAGS;
  
  if (!tags || tags.length === 0)
    return (
      <div className="flex flex-wrap gap-2 mb-1">
        <div className="badge border-none skeleton min-w-26" />
        <div className="badge border-none skeleton min-w-26" />
        <div className="badge border-none skeleton min-w-26" />
        <div className="badge border-none skeleton min-w-10" />
      </div>
    );

  return (
    <div className="flex flex-wrap gap-2 mb-1">
      {visibleTags.map((tag) => (
        <div
          key={tag.name}
          className="badge badge-soft badge-primary group-[.is-premium]:badge-warning"
          title={tag.name}
        >
          {tag.name}
        </div>
      ))}
      {extraTagsCount > 0 && (
        <div
          className="badge badge-soft badge-primary tooltip tooltip-primary group-[.is-premium]:tooltip-warning group-[.is-premium]:badge-warning tooltip-bottom lg:tooltip-right"
          data-tip={tags
            .map((tag) => tag.name)
            .slice(MAX_VISIBLE_TAGS)
            .join(", ")}
        >
          +{extraTagsCount}
        </div>
      )}
    </div>
  );
}
