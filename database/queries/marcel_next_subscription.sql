SELECT * FROM subscription_with_state
WHERE subscriber = 'a1b2c3d4-e5f6-7890-1234-56789abcdef0'
AND NOT actived AND started_in > CURRENT_DATE
ORDER BY started_in ASC;