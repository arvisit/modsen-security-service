INSERT INTO users(username, password, enabled) VALUES
    ('john_doe@mail.com', '$2a$12$zNwV0anPsveKM4kJdPKSG.YVjF9FM.dXEVB8KfrJgYDTz0ExC1ds.', true),
    ('jane_doe@mail.com', '$2a$12$zNwV0anPsveKM4kJdPKSG.YVjF9FM.dXEVB8KfrJgYDTz0ExC1ds.', true),
    ('janie_doe@mail.com', '$2a$12$zNwV0anPsveKM4kJdPKSG.YVjF9FM.dXEVB8KfrJgYDTz0ExC1ds.', true);
INSERT INTO users_roles(user_id, role) VALUES
    ((SELECT u.id FROM users u WHERE u.username = 'john_doe@mail.com'), 'USER'),
    ((SELECT u.id FROM users u WHERE u.username = 'john_doe@mail.com'), 'ADMIN'),
    ((SELECT u.id FROM users u WHERE u.username = 'jane_doe@mail.com'), 'ADMIN'),
    ((SELECT u.id FROM users u WHERE u.username = 'janie_doe@mail.com'), 'SERVICE');
