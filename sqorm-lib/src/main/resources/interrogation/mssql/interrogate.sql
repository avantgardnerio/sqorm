-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' AS classpath,
  table_name
FROM INFORMATION_SCHEMA.TABLES
WHERE TABLE_CATALOG = 'sqorm'
ORDER BY table_name;

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' AS classpath,
  table_name,
  column_name,
  DATA_TYPE
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_CATALOG = 'sqorm'
ORDER BY table_name, column_name;

-- Relations
SELECT
  'net.squarelabs.sqorm.codegen.model.Relation' AS classpath,
  fkn.name                                      AS constraint_name,
  pkn.name                                      AS primary_table,
  ckn.name                                      AS foreign_table
FROM sysforeignkeys fk
  INNER JOIN sysobjects fkn ON fk.constid = fkn.id
  INNER JOIN sysobjects pkn ON fk.rkeyid = pkn.id
  INNER JOIN sysobjects ckn ON fk.fkeyid = ckn.id
ORDER BY constraint_name;

-- RelationFields
SELECT
  'net.squarelabs.sqorm.codegen.model.RelationField' AS classpath,
  fkn.name                                           AS CONSTRAINT_NAME,
  pcol.name                                          AS primary_column,
  ccol.name                                          AS foreign_column,
  fk.keyno                                           AS ORDINAL_POSITION
FROM sysforeignkeys fk
  INNER JOIN sysobjects fkn ON fk.constid = fkn.id
  INNER JOIN sysobjects pkn ON fk.rkeyid = pkn.id
  INNER JOIN sysobjects ckn ON fk.fkeyid = ckn.id
  INNER JOIN syscolumns pcol ON fk.rkeyid = pcol.id AND fk.rkey = pcol.colid
  INNER JOIN syscolumns ccol ON fk.fkeyid = ccol.id AND fk.fkey = ccol.colid
ORDER BY CONSTRAINT_NAME, ORDINAL_POSITION;