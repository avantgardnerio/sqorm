select
	tab.table_name,
	col.column_name,
	col.ordinal_position,
	col.column_default,
	col.is_nullable,
	col.data_type,
	col.is_identity,
	case
		when pk.column_name is null then 'false'
		else 'true'
	end as "primary_key",
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
