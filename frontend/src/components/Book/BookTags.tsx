interface Props {
  tags?: string[];
}

const DEFAULT_TAGS: string[] = [];

export default function BookTag({ tags = DEFAULT_TAGS }: Props) {
  const MAX_VISIBLE_TAGS = 3;
  const visibleTags = tags.slice(0, MAX_VISIBLE_TAGS);
  const extraTagsCount = tags.length - MAX_VISIBLE_TAGS;

  return (
    <div className="flex flex-wrap sm:flex-nowrap gap-2 mb-1">
      {visibleTags.map((tag) => (
        <div
          key={tag}
          className="badge badge-outline badge-primary"
          title={tag}
        >
          {tag}
        </div>
      ))}

      {extraTagsCount > 0 && (
        <div
          className="badge badge-outline badge-secondary tooltip tooltip-secondary tooltip-bottom lg:tooltip-right"
          data-tip={tags.slice(MAX_VISIBLE_TAGS).join(", ")}
        >
          +{extraTagsCount}
        </div>
      )}
    </div>
  );
}
