CREATE OR REPLACE FUNCTION create_monthly_show_seat_partition()
RETURNS void AS $$
DECLARE
    -- Calculate the start date of the next month
    next_month_start date := date_trunc('month', NOW() + interval '1 month');
    -- Calculate the end date (start of the month after next)
    next_month_end   date := date_trunc('month', NOW() + interval '2 months');
    -- Generate the partition table name, e.g., 'scheduled_show_seat_y2025m10'
    partition_name   text := 'scheduled_show_seat_y' || to_char(next_month_start, 'YYYY') || 'm' || to_char(next_month_start, 'MM');
BEGIN
    -- Check if the partition already exists
    IF NOT EXISTS (SELECT FROM pg_class WHERE relname = partition_name AND relkind = 'r') THEN
        -- Use EXECUTE with FORMAT to safely construct and run the DDL
        EXECUTE format(
            'CREATE TABLE %I PARTITION OF public.scheduled_show_seat FOR VALUES FROM (%L) TO (%L);',
            partition_name,
            next_month_start,
            next_month_end
        );
        RAISE NOTICE 'Created partition: %', partition_name;
    ELSE
        RAISE NOTICE 'Partition % already exists.', partition_name;
    END IF;
END;
$$ LANGUAGE plpgsql;