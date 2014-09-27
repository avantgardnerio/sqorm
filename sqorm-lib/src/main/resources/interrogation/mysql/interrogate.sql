-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' AS `classpath`,
  table_name                                 AS `table_name`
FROM INFORMATION_SCHEMA.TABLES
WHERE table_schema = 'sqorm';

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' AS `classpath`,
  table_name                                  AS `table_name`,
  column_name                                 AS `column_name`,
  data_type                                   AS `data_type`,
  character_maximum_length                    AS `max_length`,
  (CASE WHEN is_nullable = 'YES' THEN TRUE
   ELSE FALSE END)                            AS `nullable`,
  0                                           AS `identity`,
  numeric_precision                           AS `precision`,
  numeric_scale                               AS `scale`,
  column_default                              AS `default_value`,
  NULL                                        AS `seed`,
  NULL                                        AS `increment`,
  (CASE WHEN column_key = 'PRI' THEN 1
   ELSE 0 END)                                AS `primary_key`
FROM INFORMATION_SCHEMA.COLUMNS
WHERE table_schema = 'sqorm';

-- Relations
SELECT
  'net.squarelabs.sqorm.codegen.model.Relation' AS classpath,
  rel.constraint_name,
  rel.table_name                                AS primary_table,
  rel.REFERENCED_TABLE_NAME                     AS foreign_table
FROM information_schema.referential_constraints rel
ORDER BY rel.constraint_name;

-- RelationFields
SELECT
  'net.squarelabs.sqorm.codegen.model.RelationField' AS classpath,
  pri.CONSTRAINT_NAME,
  pri.ORDINAL_POSITION,
  pri.COLUMN_NAME                                    AS primary_column,
  pri.REFERENCED_COLUMN_NAME                         AS foreign_column
FROM information_schema.key_column_usage pri
WHERE pri.POSITION_IN_UNIQUE_CONSTRAINT IS NOT NULL
ORDER BY pri.CONSTRAINT_NAME, pri.ORDINAL_POSITION;