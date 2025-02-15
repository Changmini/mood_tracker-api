INSERT INTO users (username, password_hash, email)
SELECT 'test'
    , '$2a$10$42h1zqT460BZi3Ivqd/tqe8yTvyVc7haDAYdRRr4dfIEuyobM/OyC' -- (= test)
    , 'test@exmple.com'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'test');