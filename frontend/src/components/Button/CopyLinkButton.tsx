import { useState } from 'react';
import { FaLink } from 'react-icons/fa';

type CopyLinkButtonProps = {
    code: string;
};

function CopyLinkButton(
    { code }: CopyLinkButtonProps
) {

    const [copied, setCopied] = useState(false);

    const copyLink = () => {
        navigator.clipboard.writeText(code)
        .then(() => {
            setCopied(true);
            setTimeout(() => setCopied(false), 2000);
        });
    };

    return (
        <div className='absolute right-5 top-5'>
        <button
            onClick={copyLink}
            className='btn btn-block'
        >
            <FaLink />
        </button>

        {copied && (
            <div style={{
            position: 'absolute',
            top: '-35px',
            left: '50%',
            transform: 'translateX(-50%)',
            backgroundColor: '#333',
            color: '#fff',
            padding: '6px 10px',
            borderRadius: '4px',
            fontSize: '12px',
            whiteSpace: 'nowrap',
            boxShadow: '0 2px 6px rgba(0, 0, 0, 0.2)'
            }}>
                Copiado!
            </div>
        )}
    </div>
  );
}

export default CopyLinkButton;
