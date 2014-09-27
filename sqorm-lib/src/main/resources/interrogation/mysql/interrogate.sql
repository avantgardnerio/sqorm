-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' AS classpath,
  table_name
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_SCHEMA = 'sqorm'
ORDER BY table_name;

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' AS classpath,
  table_name,
  column_name,
  data_type
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_SCHEMA = 'sqorm'
ORDER BY table_name, column_name;

-- Relations
SELECT
  'net.squarelabs.sqorm.codegen.model.Relation' AS classpath,
  constraint_name,
  table_name                                    AS primary_table,
  REFERENCED_TABLE_NAME                         AS foreign_table
FROM information_schema.referential_constraints rel
ORDER BY constraint_name;

-- RelationFields
SELECT
  'net.squarelabs.sqorm.codegen.model.RelationField' AS classpath,
  CONSTRAINT_NAME,
  ORDINAL_POSITION,
  COLUMN_NAME                                        AS primary_column,
  REFERENCED_COLUMN_NAME                             AS foreign_column
FROM information_schema.key_column_usage pri
WHERE POSITION_IN_UNIQUE_CONSTRAINT IS NOT NULL
ORDER BY CONSTRAINT_NAME, ORDINAL_POSITION;