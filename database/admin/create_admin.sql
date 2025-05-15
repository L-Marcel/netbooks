INSERT INTO Users(email, password, name, access) 
VALUES ('admin@gmail.com', 'admin', 'Admin', 1)
ON CONFLICT DO NOTHING;

-- A senha é temporária, futuramente vamos ter que usar criptografia
-- Não se armazena senha no banco de dados de forma bruta