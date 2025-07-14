import { useState } from "react";
import { FaLink } from "react-icons/fa";
import Button from ".";

interface Props {
  code: string;
}

function CopyLinkButton({ code }: Props) {
  const [copied, setCopied] = useState(false);

  const copyLink = () =>
    navigator.clipboard.writeText(code).then(() => {
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    });

  return (
    <div className="absolute right-5 top-5">
      <div
        data-tip={copied ? "Copiado!" : ""}
        className={`tooltip tooltip-left tooltip-primary ${copied ? "tooltip-open" : ""}`}
      >
        <Button onClick={copyLink} className="btn btn-block">
          <FaLink />
        </Button>
      </div>
    </div>
  );
}

export default CopyLinkButton;
