-- Tables
select
  'net.squarelabs.sqorm.codegen.model.Table' as "classpath",
  tab.table_name as "table_name",
  d.description as "description"
from information_schema.tables tab
  left join pg_catalog.pg_class c
    on c.relname=tab.table_name
  left join pg_catalog.pg_description d
    on d.objoid=c.oid
       and d.objsubid=0
where tab.table_schema='public'
order by tab.table_name
;

-- Columns
select
  'net.squarelabs.sqorm.codegen.model.Column' as "classpath",
  tab.table_name,
  col.column_name,
  col.ordinal_position,
  col.column_default,
  (case when is_nullable = 'YES' then true else false end) as is_nullable,
  case
      when col.data_type = 'integer' then 'int'
      when col.data_type = 'character varying' then 'String'
      when col.data_type = 'uuid' then 'UUID'
      else col.data_type
  end as data_type,
  (case when is_identity = 'YES' then true else false end) as is_identity,
  pk.ordinal_position
from information_schema.tables tab
inner join information_schema.columns col
  on col.table_name=tab.table_name
  left join (
              select
                tc.constraint_name,
                kcu.table_name,
                kcu.column_name,
                kcu.ordinal_position
              from information_schema.table_constraints tc
                left join information_schema.key_column_usage kcu
                  on tc.constraint_name = kcu.constraint_name
              where tc.constraint_type = 'PRIMARY KEY'
            ) pk
    on tab.table_name=pk.table_name
       and col.column_name=pk.column_name
where tab.table_schema='public'
order by tab.table_name, col.ordinal_position
;

-- Relations
select
  'net.squarelabs.sqorm.codegen.model.Relation' as classpath,
  rel.constraint_name as name,
  pri.table_name as primary_table,
  fr.table_name as foreign_table
from information_schema.referential_constraints rel
  inner join information_schema.table_constraints pri
    on rel.unique_constraint_name = pri.constraint_name
  inner join information_schema.table_constraints fr
    on rel.constraint_name = fr.constraint_name
order by rel.constraint_name
;

-- RelationFields
select
  'net.squarelabs.sqorm.codegen.model.RelationField' as classpath,
  rel.constraint_name,
  pri.column_name as primary_column,
  fr.column_name as foreign_column,
  fr.position_in_unique_constraint as ordinal_position
from information_schema.referential_constraints rel
  inner join information_schema.key_column_usage pri
    on pri.constraint_name = rel.unique_constraint_name
  inner join information_schema.key_column_usage fr
    on fr.constraint_name = rel.constraint_name
       and fr.position_in_unique_constraint = pri.ordinal_position
order by rel.constraint_name, fr.position_in_unique_constraint
;
