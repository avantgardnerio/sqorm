create table `Customer` (
	`CustomerId` binary(16) not null,
	`name` varchar(50) not null,
	`version` int not null,
	primary key(`CustomerId`)
);