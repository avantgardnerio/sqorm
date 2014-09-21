select
		'net.squarelabs.sqorm.codegen.Table' as classpath,
		tab.table_name as "tableName",
		d.description
from information_schema.tables tab
		left join pg_catalog.pg_class c
				on c.relname=tab.table_name
		left join pg_catalog.pg_description d
				on d.objoid=c.oid
					 and d.objsubid=0
where tab.table_schema='public'
			and tab.table_catalog='thinktank_db'
order by tab.table_name
;

select
		'net.squarelabs.sqorm.codegen.Column' as classpath,
		tab.table_name as "tableName",
		col.column_name as "columnName",
		col.ordinal_position as "sortOrder",
		col.column_default as "defaultValue",
		case
		when col.is_nullable='NO' then false
		else true
		end as "nullable",
		case
		when col.udt_name='bool' then 'boolean'
		when col.udt_name='varchar' then 'String'
		when col.udt_name='_varchar' then 'String'
		when col.udt_name='int4' then 'int'
		when col.udt_name='int8' then 'long'
		when col.udt_name='oid' then 'long'
		when col.udt_name='float4' then 'float'
		when col.udt_name='float8' then 'double'
		when col.udt_name='timestamp' then 'Date'
		when col.udt_name='interval' then 'Duration'
		else col.udt_name
		end as "type",
		case
		when col.data_type='ARRAY' then 'true'
		else 'false'
		end as "multi",
		case
		when col.is_identity='NO' then false
		else true
		end as "identity",
		case
		when col.is_generated='NO' then false
		else true
		end as "generated",
		d.description,
		rel.foreign_table_name as "relatedBean",
		rel.foreign_column_name as "relatedField",
		case
				when pk.column_name is null then 'false'
				else 'true'
		end as "isPrimaryKey"
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
									order by table_name, constraint_name
							) pk
				on tab.table_name=pk.table_name
					 and col.column_name=pk.column_name
		left join pg_catalog.pg_class c
				on c.relname=tab.table_name
		left join pg_catalog.pg_description d
				on d.objoid=c.oid
					 and d.objsubid=col.ordinal_position
		left join (
									select
											tc.constraint_name,
											tc.table_name,
											kcu.column_name,
											ccu.table_name AS foreign_table_name,
											ccu.column_name AS foreign_column_name
									from (
													 select
															 tc.constraint_name,
															 count(*) cnt
													 from information_schema.table_constraints tc
															 inner join information_schema.key_column_usage kcu
																	 on tc.constraint_name = kcu.constraint_name
															 inner join information_schema.constraint_column_usage ccu
																	 on ccu.constraint_name = tc.constraint_name
													 where constraint_type = 'FOREIGN KEY'
													 group by tc.constraint_name
											 ) cnt
											inner join information_schema.table_constraints tc
													on tc.constraint_name=cnt.constraint_name
											inner join information_schema.key_column_usage kcu
													on tc.constraint_name = kcu.constraint_name
											inner join information_schema.constraint_column_usage ccu
													on ccu.constraint_name = tc.constraint_name
									where cnt.cnt=1 -- TODO: Allow keys with multiple columns
							) rel
				on rel.table_name=tab.table_name
					 and rel.column_name=col.column_name
where tab.table_schema='public'
			and tab.table_catalog='sqorm'
order by
		tab.table_name,
		col.ordinal_position
;