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
  (case when pk.column_name is null then false else true end) as "primary_key",
  rel.foreign_table_name as "foreign_table",
  rel.foreign_column_name as "foreign_column"
from information_schema.tables tab
  inner join information_schema.columns col
    on col.table_name=tab.table_name
  left join (
              select
                tc.constraint_name,
                kcu.table_name,
                kcu.column_name
              from information_schema.table_constraints tc
                left join information_schema.key_column_usage kcu
                  on tc.constraint_name = kcu.constraint_name
              where tc.constraint_type = 'PRIMARY KEY'
            ) pk
    on tab.table_name=pk.table_name
       and col.column_name=pk.column_name
  left join (
              select
                tc.constraint_name,
                tc.table_name,
                kcu.column_name,
                ccu.table_name AS foreign_table_name,
                ccu.column_name AS foreign_column_name
              from information_schema.table_constraints tc
                inner join information_schema.key_column_usage kcu
                  on tc.constraint_name = kcu.constraint_name
                inner join information_schema.constraint_column_usage ccu
                  on ccu.constraint_name = tc.constraint_name
            ) rel
    on rel.table_name=tab.table_name
       and rel.column_name=col.column_name
where tab.table_schema='public'
order by tab.table_name, col.ordinal_position
;
