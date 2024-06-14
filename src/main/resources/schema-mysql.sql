CREATE TABLE if not exists `person_info` (
  `id` varchar(20) NOT NULL,
  `name` varchar(45) NOT NULL,
  `age` int NOT NULL DEFAULT '0',
  `city` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
