select
  *
from (
  select
    ROW_NUMBER() over (order by [name]) as rownum,
    c.*
    from customer c
) as rs
where rs.rownum >= @PageNumber * @PageSize
  and rs.rownum < (@PageNumber+1) * @PageSize
