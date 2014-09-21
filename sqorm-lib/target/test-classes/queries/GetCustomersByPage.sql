select
  c.*
from customer
order by name
limit @PageSize
offset @PageNum * @PageSize
