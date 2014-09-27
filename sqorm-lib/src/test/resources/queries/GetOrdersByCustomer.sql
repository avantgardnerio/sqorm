select
  'net.squarelabs.sqorm.model.Customer' as classpath,
  c.*
from customer c
where c.[CustomerId]=@CustomerId
;

select
  'net.squarelabs.sqorm.model.Order' as classpath,
  o.*
from customer c
inner join orders o
  on o.customer_id=c.[CustomerId]
where c.[CustomerId]=@CustomerId
;

select
  'net.squarelabs.sqorm.model.OrderDetails' as classpath,
  od.*
from customer c
inner join orders o
  on o.customer_id=c.[CustomerId]
inner join [OrderDetails] od
  on od.order_id=o.order_id
where c.[CustomerId]=@CustomerId
;
