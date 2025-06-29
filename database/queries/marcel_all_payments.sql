SELECT pay.* FROM payment AS pay
JOIN subscription AS sub
ON sub.id = pay.subscription
WHERE sub.subscriber = '09876543-2109-fedc-ba98-7654321fedcb';