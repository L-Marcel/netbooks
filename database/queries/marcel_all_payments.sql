SELECT pay.* FROM payment AS pay
JOIN subscription AS sub
ON sub.id = pay.subscription
WHERE sub.subscriber = 'a1b2c3d4-e5f6-7890-1234-56789abcdef0';