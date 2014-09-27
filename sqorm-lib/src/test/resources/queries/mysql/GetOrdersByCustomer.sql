select
  'net.squarelabs.sqorm.model.Customer' as classpath,
  hex(c.`CustomerId`) as `CustomerId`,
  c.name,
  c.version
from `Customer` c
where c.`CustomerId`=unhex(@CustomerId)
;

select
  'net.squarelabs.sqorm.model.Order' as classpath,
  o.order_id,
  hex(o.customer_id) as customer_id,
  o.version
from `Customer` c
inner join `Orders` o
  on o.customer_id=c.`CustomerId`
where c.`CustomerId`=unhex(@CustomerId)
;

select
  'net.squarelabs.sqorm.model.OrderDetails' as classpath,
  od.*
from `Customer` c
inner join `Orders` o
  on o.customer_id=c.`CustomerId`
inner join `OrderDetails` od
  on od.order_id=o.order_id
where c.`CustomerId`=unhex(@CustomerId)
;
