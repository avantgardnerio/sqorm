-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' AS "classpath",
  tab.table_name                             AS "table_name",
  d.description                              AS "description"
FROM information_schema.tables tab
  LEFT JOIN pg_catalog.pg_class c
    ON c.relname = tab.table_name
  LEFT JOIN pg_catalog.pg_description d
    ON d.objoid = c.oid
       AND d.objsubid = 0
WHERE tab.table_schema = 'public'
ORDER BY tab.table_name;

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' AS "classpath",
  tab.table_name,
  col.column_name,
  col.ordinal_position,
  col.column_default,
  (CASE WHEN is_nullable = 'YES' THEN TRUE
   ELSE FALSE END)                            AS is_nullable,
  CASE
  WHEN col.data_type = 'integer' THEN 'int'
  WHEN col.data_type = 'character varying' THEN 'String'
  WHEN col.data_type = 'uuid' THEN 'UUID'
  ELSE col.data_type
  END                                         AS data_type,
  (CASE WHEN is_identity = 'YES' THEN TRUE
   ELSE FALSE END)                            AS is_identity,
  pk.ordinal_position                         AS "pk_ordinal"
FROM information_schema.tables tab
  INNER JOIN information_schema.columns col
    ON col.table_name = tab.table_name
  LEFT JOIN (
              SELECT
                tc.constraint_name,
                kcu.table_name,
                kcu.column_name,
                kcu.ordinal_position
              FROM information_schema.table_constraints tc
                LEFT JOIN information_schema.key_column_usage kcu
                  ON tc.constraint_name = kcu.constraint_name
              WHERE tc.constraint_type = 'PRIMARY KEY'
            ) pk
    ON tab.table_name = pk.table_name
       AND col.column_name = pk.column_name
WHERE tab.table_schema = 'public'
ORDER BY tab.table_name, col.ordinal_position;

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
ORDER BY rel.constraint_name, fr.position_in_unique_constraint;
