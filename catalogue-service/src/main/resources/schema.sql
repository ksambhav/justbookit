--DROP VIEW IF EXISTS now_showing;
--DROP VIEW IF EXISTS all_scheduled_shows;


--DROP TABLE IF EXISTS screening_instance;




--DROP TABLE IF EXISTS scheduled_screening;
--CREATE TABLE scheduled_screening (
--    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
--    movie_id VARCHAR(255) NOT NULL,
--    city VARCHAR(255) NOT NULL,
--    movie_title VARCHAR(1000) NOT NULL,
--    venue_id UUID,
--    venue_name VARCHAR(255),
--    theater_id UUID,
--    start_time TIME NOT NULL,
--    end_time TIME NOT NULL,
--    movie_poster_url VARCHAR(1000),
--    start_date DATE NOT NULL,
--    end_date DATE NOT NULL,
--    max_capacity int,
--    is_houseful boolean DEFAULT false,
--    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
--    search_vector tsvector GENERATED ALWAYS AS (
--        setweight(to_tsvector('english', coalesce(movie_title, '')), 'A') ||
--        setweight(to_tsvector('english', coalesce(city, '')), 'B') ||
--        setweight(to_tsvector('english', coalesce(venue_name, '')), 'C')
--    ) STORED,
--    UNIQUE (movie_id,city,venue_id,theater_id,start_time)
--);

--ALTER TABLE scheduled_screening
--ADD COLUMN IF NOT EXISTS search_vector tsvector GENERATED ALWAYS AS (
--    setweight(to_tsvector('english', coalesce(movie_title, '')), 'A') ||
--    setweight(to_tsvector('english', coalesce(city, '')), 'B') ||
--    setweight(to_tsvector('english', coalesce(venue_name, '')), 'C')
--) STORED;
--CREATE INDEX  IF NOT EXISTS idx_scheduled_screening_start_date ON scheduled_screening(start_date);
--CREATE INDEX  IF NOT EXISTS idx_scheduled_screening_search_vector ON scheduled_screening USING GIN(search_vector);
--CREATE INDEX  IF NOT EXISTS idx_scheduled_screening_city ON scheduled_screening(city);
--CREATE INDEX  IF NOT EXISTS idx_scheduled_screening_movie_title ON scheduled_screening(movie_title);
--CREATE INDEX  IF NOT EXISTS idx_scheduled_screening_venue_name ON scheduled_screening(venue_name);
--CREATE TABLE IF NOT EXISTS screening_instance
--(
--    id uuid NOT NULL,
--    city character varying(255) COLLATE pg_catalog."default",
--    date_of_screening date,
--    end_time time(6) without time zone,
--    is_cancelled boolean NOT NULL,
--    movie_id character varying(255) COLLATE pg_catalog."default",
--    movie_poster_url character varying(255) COLLATE pg_catalog."default",
--    movie_title character varying(255) COLLATE pg_catalog."default",
--    start_time time(6) without time zone,
--    theater_id character varying(255) COLLATE pg_catalog."default",
--    venue_id character varying(255) COLLATE pg_catalog."default",
--    venue_name character varying(255) COLLATE pg_catalog."default",
--    scheduled_screening_id uuid NOT NULL,
--    CONSTRAINT screening_instance_pkey PRIMARY KEY (id),
--    CONSTRAINT fkbruac5crwgk1tvxkyuqfppvrg FOREIGN KEY (scheduled_screening_id)
--        REFERENCES scheduled_screening (id) MATCH SIMPLE
--        ON UPDATE NO ACTION
--        ON DELETE NO ACTION
--);

alter table if exists scheduled_show add column if not exists sold_out boolean DEFAULT FALSE;
alter table if exists scheduled_show add column if not exists cancelled boolean DEFAULT FALSE;

CREATE TABLE IF NOT EXISTS public.scheduled_show_seat
(
    id character varying(255) NOT NULL,
    show_date date NOT NULL, -- The partition key cannot be nullable
    movie_id uuid,
    seat_id character varying(255),
    start_time time(6) without time zone,
    theatre_id uuid,
    status character varying(255),
    show_id character varying(255) NOT NULL,

    -- The partition key 'show_date' is now part of the primary key.
    -- Placing it first is often better for performance if your queries filter by date.
    CONSTRAINT scheduled_show_seat_pkey PRIMARY KEY (show_date, id),

    CONSTRAINT fk594b3dig6muv4ri7ldcgohlwc FOREIGN KEY (show_id)
        REFERENCES public.scheduled_show (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,

    CONSTRAINT scheduled_show_seat_status_check CHECK (status::text = ANY (ARRAY['BOOKED'::character varying, 'HOLD'::character varying, 'AVAILABLE'::character varying]::text[]))
)
PARTITION BY RANGE (show_date);
-- This clause declares it as a partitioned table

-- Optional: Add a comment to describe the table's purpose
COMMENT ON TABLE public.scheduled_show_seat IS 'Master partitioned table for show seats. Data is stored in monthly child partitions.';



---- Partition for September 2025
--CREATE TABLE scheduled_show_seat_y2025m09
--PARTITION OF public.scheduled_show_seat
--FOR VALUES FROM ('2025-09-01') TO ('2025-10-01');
--
---- Partition for October 2025
--CREATE TABLE scheduled_show_seat_y2025m10
--PARTITION OF public.scheduled_show_seat
--FOR VALUES FROM ('2025-10-01') TO ('2025-11-01');
--
---- Partition for November 2025
--CREATE TABLE scheduled_show_seat_y2025m11
--PARTITION OF public.scheduled_show_seat
--FOR VALUES FROM ('2025-11-01') TO ('2025-12-01');
--
--
---- Partition for December 2025
--CREATE TABLE scheduled_show_seat_y2025m12
--PARTITION OF public.scheduled_show_seat
--FOR VALUES FROM ('2025-12-01') TO ('2026-01-01');



CREATE OR REPLACE VIEW now_showing
 AS
 SELECT id,
    city,
    end_time,
    movie_title,
    movie_id,
    show_date,
    start_time,
    theatre_id,
    venue,
    show_time,
    cancelled,
    sold_out
   FROM scheduled_show ss
  WHERE CURRENT_TIMESTAMP < show_time AND cancelled IS FALSE AND sold_out IS FALSE
  ORDER BY show_time, venue;

create or replace view now_showing_summary
as
select
DISTINCT movie_id,show_date,movie_title,city,m.poster_url
from now_showing
LEFT OUTER JOIN movie m on m.id=movie_id
order by show_date,city;


create or replace view theatre_venue
as
select t.id  as theatre_id ,t.name as theatre_name, v.city, v.name as venue_name,v.id as venue_id
from theatre t
join venue v on t.venue_id = v.id;
