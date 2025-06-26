SELECT * FROM subscription_with_state
WHERE subscriber = '09876543-2109-fedc-ba98-7654321fedcb'
AND NOT actived AND started_in > CURRENT_DATE
ORDER BY started_in ASC;