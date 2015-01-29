-- Tables
SELECT
  'net.squarelabs.sqorm.codegen.model.Table' AS classpath,
  tab.name                                   AS table_name
FROM sysobjects tab
WHERE tab.type = 'U'
ORDER BY table_name;

-- Columns
SELECT
  'net.squarelabs.sqorm.codegen.model.Column' AS classpath,
  tab.name                                    AS table_name,
  col.name                                    AS column_name,
  CASE
  WHEN typ.name = 'image' THEN 'Byte[]'
  WHEN typ.name = 'varbinary' THEN 'Byte[]'
  WHEN typ.name = 'nchar' THEN 'BigDecimal'
  WHEN typ.name = 'money' THEN 'BigDecimal'
  WHEN typ.name = 'smallmoney' THEN 'BigDecimal'
  WHEN typ.name = 'decimal' THEN 'BigDecimal'
  WHEN typ.name = 'bit' THEN 'Boolean'
  WHEN typ.name = 'sysname' THEN 'String'
  WHEN typ.name = 'varchar' THEN 'String'
  WHEN typ.name = 'nvarchar' THEN 'String'
  WHEN typ.name = 'text' THEN 'String'
  WHEN typ.name = 'bigint' THEN 'Long'
  WHEN typ.name = 'smallint' THEN 'Short'
  WHEN typ.name = 'tinyint' THEN 'Byte'
  WHEN typ.name = 'date' THEN 'Instant'
  WHEN typ.name = 'datetime' THEN 'Instant'
  WHEN typ.name = 'datetime2' THEN 'Instant'
  WHEN typ.name = 'smalldatetime' THEN 'Instant'
  WHEN typ.name = 'uniqueidentifier' THEN 'UUID'
  ELSE typ.name
  END                                         AS data_type,
  col.colstat & 1                             AS auto_increment,
  case
    when ic.key_ordinal is not null then ic.key_ordinal-1
    else NULL
  end as pk_ordinal
FROM sysobjects tab
INNER JOIN syscolumns col
  ON col.id = tab.id
INNER JOIN systypes typ
  ON typ.xtype = col.xtype
  AND typ.xusertype = col.xusertype
Inner Join sys.indexes i
	On tab.id = i.object_id
	And i.is_primary_key = 1
left Join sys.index_columns ic
	On i.object_id = ic.object_id
	And i.index_id = ic.index_id
	and ic.column_id=col.colid
WHERE tab.type = 'U'
ORDER BY table_name, column_name

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