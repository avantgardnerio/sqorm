-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' AS classpath,
  table_name
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_CATALOG = 'sqorm' AND TABLE_SCHEMA = 'public'
ORDER BY table_name;

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' AS classpath,
  table_name,
  column_name,
  data_type,
  CASE WHEN column_default LIKE 'nextval%' THEN TRUE
  ELSE FALSE END                              AS auto_increment
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_CATALOG = 'sqorm' AND TABLE_SCHEMA = 'public'
ORDER BY table_name, column_name;

-- Relations
SELECT
  'net.squarelabs.sqorm.codegen.model.Relation' AS classpath,
  rel.constraint_name,
  pri.table_name                                AS primary_table,
  fr.table_name                                 AS foreign_table
FROM information_schema.referential_constraints rel
  INNER JOIN information_schema.table_constraints pri
    ON rel.unique_constraint_name = pri.constraint_name
  INNER JOIN information_schema.table_constraints fr
    ON rel.constraint_name = fr.constraint_name
WHERE rel.CONSTRAINT_CATALOG = 'sqorm' AND rel.CONSTRAINT_SCHEMA = 'public'
ORDER BY rel.constraint_name;

-- RelationFields
SELECT
  'net.squarelabs.sqorm.codegen.model.RelationField' AS classpath,
  rel.constraint_name,
  pri.column_name                                    AS primary_column,
  fr.column_name                                     AS foreign_column,
  fr.position_in_unique_constraint                   AS ordinal_position
FROM information_schema.referential_constraints rel
  INNER JOIN information_schema.key_column_usage pri
    ON pri.constraint_name = rel.unique_constraint_name
  INNER JOIN information_schema.key_column_usage fr
    ON fr.constraint_name = rel.constraint_name
       AND fr.position_in_unique_constraint = pri.ordinal_position
WHERE rel.CONSTRAINT_CATALOG = 'sqorm' AND rel.CONSTRAINT_SCHEMA = 'public'
ORDER BY rel.constraint_name, fr.position_in_unique_constraint;
