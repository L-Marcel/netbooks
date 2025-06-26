SELECT DISTINCT pay.* FROM payment AS pay
JOIN subscription AS sub
ON pay.subscription = sub.id
WHERE sub.subscriber = '09876543-2109-fedc-ba98-7654321fedcb'
ORDER BY pay.created_at DESC, pay.id DESC;