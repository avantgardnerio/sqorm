CREATE TABLE `Customer` (
  `CustomerId` BINARY(16)  NOT NULL,
  `name`       VARCHAR(50) NOT NULL,
  `version`    INT         NOT NULL,
  PRIMARY KEY (`CustomerId`)
);

CREATE TABLE `Orders` (
  `order_id`    INT        NOT NULL AUTO_INCREMENT,
  `customer_id` BINARY(16) NOT NULL,
  `version`     INT        NOT NULL,
  PRIMARY KEY (`order_id`),
  FOREIGN KEY (`customer_id`) REFERENCES `Customer` (`CustomerId`)
);

CREATE TABLE `OrderDetails` (
  `OrderDetailsId` INT NOT NULL AUTO_INCREMENT,
  `order_id`       INT NOT NULL,
  `version`        INT NOT NULL,
  PRIMARY KEY (`OrderDetailsId`),
  FOREIGN KEY (`order_id`) REFERENCES `Orders` (`order_id`)
);

CREATE TABLE `row` (
  `RowId`   INT         NOT NULL,
  `Name`    VARCHAR(50) NOT NULL,
  `version` INT         NOT NULL,
  PRIMARY KEY (`RowId`)
);

CREATE TABLE `column` (
  `ColumnId` INT         NOT NULL,
  `Name`     VARCHAR(50) NOT NULL,
  `version`  INT         NOT NULL,
  PRIMARY KEY (`ColumnId`)
);

CREATE TABLE `cell` (
  `RowId`    INT NOT NULL,
  `ColumnId` INT NOT NULL,
  PRIMARY KEY (`RowId`, `ColumnId`),
  FOREIGN KEY (`RowId`) REFERENCES `row` (`RowId`),
  FOREIGN KEY (`ColumnId`) REFERENCES `column` (`ColumnId`)
);

CREATE TABLE `cell_data` (
  `RowId`     INT NOT NULL,
  `ColumnId`  INT NOT NULL,
  `cell_text` VARCHAR(50),
  PRIMARY KEY (`RowId`, `ColumnId`),
  FOREIGN KEY (`RowId`, `ColumnId`) REFERENCES `cell` (`RowId`, `ColumnId`)
);