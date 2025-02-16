create table public.orders
(
    order_status smallint not null
        constraint orders_order_status_check
            check ((order_status >= 0) AND (order_status <= 2)),
    order_id     uuid     not null
        primary key
);


create table public.order_booking_ids
(
    booking_ids    uuid,
    order_order_id uuid not null
        constraint publicorders
            references public.orders
);


create table public.product
(
    price      numeric(38, 2),
    quantity   integer
        constraint product_quantity_check
            check (quantity >= 0),
    product_id uuid not null
        primary key,
    name       varchar(255),
    status     varchar(255)
        constraint product_status_check
            check ((status)::text = ANY ((ARRAY ['ACTIVE'::character varying, 'DELETED'::character varying])::text[])),
    unit       varchar(255)
);

create table public.booking
(
    quantity   integer,
    expires_at timestamp(6) with time zone,
    booking_id uuid not null
        primary key,
    id         uuid not null
        constraint uuidcheck
            references public.product,
    status     varchar(255)
        constraint booking_status_check
            check ((status)::text = ANY
        ((ARRAY ['NEW'::character varying, 'EXPIRED'::character varying, 'CANCELLED'::character varying, 'CONFIRMED'::character varying])::text[]))
    );


