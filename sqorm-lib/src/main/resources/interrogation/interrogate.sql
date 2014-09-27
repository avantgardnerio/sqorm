-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' AS classpath,
  table_name
FROM INFORMATION_SCHEMA.TABLES tab
WHERE (table_catalog = 'sqorm' AND TABLE_SCHEMA = 'dbo') /* T-SQL */
      OR (table_catalog = 'sqorm' AND TABLE_SCHEMA = 'public') /* PostGreSQL */
      OR (table_catalog = 'def' AND TABLE_SCHEMA = 'sqorm') /* MySQL */
;

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' AS classpath,
  table_name,
  column_name,
  data_type,
  character_maximum_length,
  is_nullable,
  numeric_precision,
  numeric_scale,
  column_default
FROM INFORMATION_SCHEMA.COLUMNS col
WHERE (table_catalog = 'sqorm' AND TABLE_SCHEMA = 'dbo') /* T-SQL */
      OR (table_catalog = 'sqorm' AND TABLE_SCHEMA = 'public') /* PostGreSQL */
      OR (table_catalog = 'def' AND TABLE_SCHEMA = 'sqorm') /* MySQL */
;

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