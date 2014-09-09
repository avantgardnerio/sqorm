USE `sqorm`;

-- customer
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- orders
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `OrderId` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`OrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

