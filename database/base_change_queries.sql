use playlist_generator_db;

DROP TABLE IF EXISTS `playlist_generator_db`.`authorities` ;
DROP TABLE IF EXISTS `playlist_generator_db`.`authority` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`authorities` (
  `authority_id` INT NOT NULL AUTO_INCREMENT,
  `role_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`authority_id`))
ENGINE = InnoDB;

insert into authorities
values(1, "ROLE_USER");

insert into authorities
values(2, "ROLE_ADMIN");

DROP TABLE IF EXISTS `playlist_generator_db`.`users` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`users` (
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `authority_id` INT NOT NULL,
  `enabled` TINYINT(1) NULL,
  `is_first_login` TINYINT(1) NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC), 
  INDEX `user_authority_relation_idx` (`authority_id` ASC) ,
  CONSTRAINT `user_authority_relation`
    FOREIGN KEY (`authority_id`)
    REFERENCES `playlist_generator_db`.`authority` (`authority_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

insert into users
values("petar", "$2a$10$eQ10G.aCGAXANUq3pt.q4ehxwloHxY/y.9iUSaxWEHIs4xhcBFKba", 1, 1, 1);

select * from authorities;

select * from users;