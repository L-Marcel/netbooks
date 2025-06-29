SELECT DISTINCT pay.* FROM payment AS pay
JOIN subscription AS sub
ON pay.subscription = sub.id
WHERE sub.subscriber = 'a1b2c3d4-e5f6-7890-1234-56789abcdef0'
ORDER BY pay.created_at DESC, pay.id DESC;