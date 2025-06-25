import { Tag } from "@models/book";

interface Props {
  tags?: Tag[];
}

const DEFAULT_TAGS: Tag[] = [];

export default function BookTag({ tags = DEFAULT_TAGS }: Props) {
  const MAX_VISIBLE_TAGS = 3;
  const visibleTags =
    tags.length > MAX_VISIBLE_TAGS ? tags.slice(0, MAX_VISIBLE_TAGS) : tags;
  const extraTagsCount = tags.length - MAX_VISIBLE_TAGS;

  return (
    <div className="flex flex-wrap sm:flex-nowrap gap-2 mb-1">
      {visibleTags.map((tag) => (
        <div
          key={tag.name}
          className="badge badge-outline badge-primary"
          title={tag.name}
        >
          {tag.name}
        </div>
      ))}
      {extraTagsCount > 0 && (
        <div
          className="badge badge-outline badge-secondary tooltip tooltip-secondary tooltip-bottom lg:tooltip-right"
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
