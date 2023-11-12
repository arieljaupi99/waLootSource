-- public.coin_seq definition

-- DROP SEQUENCE public.coin_seq;

CREATE SEQUENCE public.coin_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;