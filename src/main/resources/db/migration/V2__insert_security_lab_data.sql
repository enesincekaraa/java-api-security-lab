insert into lab_users (id, username, role)
values ('11111111-1111-1111-1111-111111111111', 'alice', 'USER'),
       ('22222222-2222-2222-2222-222222222222', 'bob', 'USER');


insert into customer_orders (id, owner_id, product_name, total_amount, currency, created_at)
values (
           'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
           '11111111-1111-1111-1111-111111111111',
           'Mekanik Klavye',
           2499.90,
           'TRY',
           '2026-09-15T10:00:00Z'
       ),
       (
           'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
           '22222222-2222-2222-2222-222222222222',
           'Oyuncu Monitörü',
           8999.90,
           'TRY',
           '2026-09-15T11:00:00Z'
       );