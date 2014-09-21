-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' as `classpath`,
  table_name as `table_name`
FROM INFORMATION_SCHEMA.TABLES
where table_schema='sqorm';

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' as `classpath`,
  table_name as `table_name`,
  column_name as `column_name`,
  data_type as `data_type`,
  character_maximum_length as `max_length`,
  (case when is_nullable = 'YES' then true else false end) as `nullable`,
  0 as `identity`,
  numeric_precision as `precision`,
  numeric_scale as `scale`,
  column_default as `default_value`,
  null as `seed`,
  null as `increment`,
  (case when column_key = 'PRI' then 1 else 0 end) as `primary_key`
FROM INFORMATION_SCHEMA.COLUMNS
where table_schema='sqorm';

