select
  'net.squarelabs.sqorm.demo.customer' as classpath,
  c.*
from customer c
where c.customer_id=@CustomerId
;

select
  'net.squarelabs.sqorm.demo.order' as classpath,
  o.*
from customer c
inner join orders o
  on o.customer_id=c.customer_id
where c.customer_id=@CustomerId
;

select
  'net.squarelabs.sqorm.demo.orderdetails' as classpath,
  od.*
from customer c
inner join orders o
  on o.customer_id=c.customer_id
inner join `OrderDetails` od
  on od.`OrderId`=o.`OrderId`
where c.customer_id=@CustomerId
;
