-- public.coin definition

-- Drop table

-- DROP TABLE public.coin;

CREATE TABLE public.coin (
                             id int8 NOT NULL,
                             completed bool NULL,
                             latitude float8 NULL,
                             longitude float8 NULL,
                             osm_id int8 NULL,
                             user_id int8 NULL,
                             CONSTRAINT coin_pkey PRIMARY KEY (id)
);