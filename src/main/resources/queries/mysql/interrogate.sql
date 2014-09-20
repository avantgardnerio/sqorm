-- Tables
SELECT
    table_name as TableName
FROM INFORMATION_SCHEMA.TABLES
where table_schema='sqorm';

-- Columns
SELECT
    table_name as TableName,
    column_name as ColumnName,
    data_type as DataType,
    character_maximum_length as Length,
    (case when is_nullable = 'YES' then 1 else 0 end) as Nullable,
    0 as Identity,
    numeric_precision as 'Precision',
    numeric_scale as Scale,
    column_default as DefaultValue,
    null as Seed,
    null as Increment,
    (case when column_key = 'PRI' then 1 else 0 end) as PrimaryKey
FROM INFORMATION_SCHEMA.COLUMNS
where table_schema='sqorm';

-- Table Relationships
select
	constraint_name as RelationName,
	referenced_table_name as ParentTable,
	table_name as ChildTable
from information_schema.KEY_COLUMN_USAGE
where referenced_table_name is not null
	and constraint_schema='sqorm'
order by ParentTable, ChildTable;

-- Column Relationships
select
	constraint_name as RelationName,
	referenced_table_name as ParentTable,
	referenced_column_name as ParentColumn,
	table_name as ChildTable,
	column_name as ChildColumn
from information_schema.KEY_COLUMN_USAGE
where referenced_table_name is not null
	and constraint_schema='sqorm'
order by ParentTable, ParentColumn, ChildTable, ChildColumn;

