-- Tables
select
  name as TableName
from sysobjects
where type='U'
order by TableName

-- Columns
select
	tab.name as TableName,
	col.name as ColumnName,
	typ.name as DataType,
	col.length as Length,
	col.isnullable as Nullable,
	col.colstat & 1 AS [Identity],
	(CASE col.colstat & 1 WHEN 1 THEN ident_seed(tab.name) ELSE NULL END) AS Seed,
	(CASE col.colstat & 1 WHEN 1 THEN ident_incr(tab.name) ELSE NULL END) AS Increment,
	(case when kc.keyno is null then 0 else 1 end) as PrimaryKey
from sysobjects tab
inner join syscolumns col on col.id=tab.id
inner join systypes typ on typ.xtype=col.xtype and typ.xusertype=col.xusertype
left join sysobjects keys on tab.id=keys.parent_obj and keys.xtype='PK'
left join sysindexes ind on ind.name=keys.name
left join sysindexkeys kc on kc.id=tab.id and kc.indid=ind.indid and kc.colid=col.colid
where tab.type='U'
order by TableName, ColumnName

-- Table Relationships
select
	fkn.name as RelationName,
	pkn.name as ParentTable,
	ckn.name as ChildTable
from sysforeignkeys fk
inner join sysobjects fkn on fk.constid=fkn.id
inner join sysobjects pkn on fk.rkeyid=pkn.id
inner join sysobjects ckn on fk.fkeyid=ckn.id
order by ParentTable, ChildTable

-- Column Relationships
select
	fkn.name as RelationName,
	pkn.name as ParentTable,
	pcol.name as ParentColumn,
	ckn.name as ChildTable,
	ccol.name as ChildColumn
from sysforeignkeys fk
inner join sysobjects fkn on fk.constid=fkn.id
inner join sysobjects pkn on fk.rkeyid=pkn.id
inner join sysobjects ckn on fk.fkeyid=ckn.id
inner join syscolumns pcol on fk.rkeyid=pcol.id and fk.rkey=pcol.colid
inner join syscolumns ccol on fk.fkeyid=ccol.id and fk.fkey=ccol.colid
order by ParentTable, ParentColumn, ChildTable, ChildColumn

