-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema playlist_generator_db
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `playlist_generator_db` ;

-- -----------------------------------------------------
-- Schema playlist_generator_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `playlist_generator_db` DEFAULT CHARACTER SET utf8 ;
USE `playlist_generator_db` ;

-- -----------------------------------------------------
-- Table `playlist_generator_db`.`artists`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`artists` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`artists` (
  `artist_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `artist_tracklist_url` VARCHAR(300) NULL,
  PRIMARY KEY (`artist_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`albums`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`albums` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`albums` (
  `album_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `artist_id` INT NOT NULL,
  `album_tracklist_url` VARCHAR(300) NULL,
  PRIMARY KEY (`album_id`),
  INDEX `album_artist_relation_idx` (`artist_id` ASC),
  CONSTRAINT `album_artist_relation`
    FOREIGN KEY (`artist_id`)
    REFERENCES `playlist_generator_db`.`artists` (`artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`genres`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`genres` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`genres` (
  `genre_id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(200) NOT NULL,
  `image_url` VARCHAR(300) NULL,
  PRIMARY KEY (`genre_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`tracks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`tracks` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`tracks` (
  `track_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `preview_url` VARCHAR(300) NULL,
  `duration` INT NOT NULL,
  `rank` INT NOT NULL,
  `link` VARCHAR(300) NOT NULL,
  `genre_id` INT NOT NULL,
  `artist_id` INT NOT NULL,
  `album_id` INT NOT NULL,
  PRIMARY KEY (`track_id`),
  INDEX `track_album_relation_idx` (`album_id` ASC) ,
  INDEX `track_genre_relation_idx` (`genre_id` ASC) ,
  INDEX `track_artist_relation_idx` (`artist_id` ASC) ,
  CONSTRAINT `track_album_relation`
    FOREIGN KEY (`album_id`)
    REFERENCES `playlist_generator_db`.`albums` (`album_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `track_genre_relation`
    FOREIGN KEY (`genre_id`)
    REFERENCES `playlist_generator_db`.`genres` (`genre_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `track_artist_relation`
    FOREIGN KEY (`artist_id`)
    REFERENCES `playlist_generator_db`.`artists` (`artist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`users` ;


CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `authority_id` INT NOT NULL,
  `enabled` TINYINT(1) NULL,
  `is_first_login` TINYINT(1) NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC), 
  INDEX `user_authority_relation_idx` (`authority_id` ASC) ,
  CONSTRAINT `user_authority_relation`
    FOREIGN KEY (`authority_id`)
    REFERENCES `playlist_generator_db`.`authorities` (`authority_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`user_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`user_details` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`user_details` (
  `user_details_id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `avatar` LONGBLOB NULL,
  `is_deleted` TINYINT(1) NULL,
  PRIMARY KEY (`user_details_id`),
  INDEX `user_details_user_relation_idx` (`user_id` ASC) ,
  CONSTRAINT `user_details_user_relation`
    FOREIGN KEY (`user_id`)
    REFERENCES `playlist_generator_db`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`playlists`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`playlists` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`playlists` (
  `playlist_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `user_id` INT NOT NULL,
  `duration` INT NOT NULL,
  `is_deleted` TINYINT(1) NULL,
  `top_genre_id` INT NOT NULL,
  PRIMARY KEY (`playlist_id`),
  INDEX `user_playlist_relation_idx` (`user_id` ASC) ,
  INDEX `playlist_top_genre_relation_idx` (`top_genre_id` ASC) ,
  CONSTRAINT `user_playlist_relation`
    FOREIGN KEY (`user_id`)
    REFERENCES `playlist_generator_db`.`user_details` (`user_details_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
CONSTRAINT `playlist_top_genre_relation`
    FOREIGN KEY (`top_genre_id`)
    REFERENCES `playlist_generator_db`.`genres` (`genre_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`genre_playlist_relations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`genre_playlist_relations` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`genre_playlist_relations` (
  `genre_laylist_relation_Id` INT NOT NULL AUTO_INCREMENT,
  `playlist_id` INT NOT NULL,
  `genre_id` INT NOT NULL,
  `is_deleted` TINYINT(1) NULL,
  PRIMARY KEY (`genre_laylist_relation_Id`),
  INDEX `genre_playlist_relation_idx` (`genre_id` ASC) ,
  INDEX `playlist_genre_ralation_idx` (`playlist_id` ASC) ,
  CONSTRAINT `genre_playlist_relation`
    FOREIGN KEY (`genre_id`)
    REFERENCES `playlist_generator_db`.`genres` (`genre_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `playlist_genre_ralation`
    FOREIGN KEY (`playlist_id`)
    REFERENCES `playlist_generator_db`.`playlists` (`playlist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`playlist_track_relations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`playlist_track_relations` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`playlist_track_relations` (
  `playlist_track_relation_id` INT NOT NULL AUTO_INCREMENT,
  `playlist_id` INT NOT NULL,
  `track_id` INT NOT NULL,
  `is_deleted` TINYINT(1) NULL,
  PRIMARY KEY (`playlist_track_relation_id`),
  INDEX `playlist_track_relation_idx` (`playlist_id` ASC) ,
  INDEX `track_playlist_relation_idx` (`track_id` ASC) ,
  CONSTRAINT `playlist_track_relation`
    FOREIGN KEY (`playlist_id`)
    REFERENCES `playlist_generator_db`.`playlists` (`playlist_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `track_playlist_relation`
    FOREIGN KEY (`track_id`)
    REFERENCES `playlist_generator_db`.`tracks` (`track_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
-- -----------------------------------------------------
-- Table `playlist_generator_db`.`authorities`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`authorities` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`authorities` (
  `authority_id` INT NOT NULL AUTO_INCREMENT,
  `role_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`authority_id`))
ENGINE = InnoDB;
insert into authorities values(1, "ROLE_USER"),(2, "ROLE_ADMIN");

insert into users 
values (1, "stoyan", "$2a$10$SUIXcLF6VA0Rw1Au7itQF.KV2gKVm4LNx0HfMAt4yXoD0Re2zTu92", 2, 1, 1), 
(2, "ventsi", "$2a$10$SUIXcLF6VA0Rw1Au7itQF.KV2gKVm4LNx0HfMAt4yXoD0Re2zTu92", 2, 1, 1);

insert into user_details
values(1, 1, "stoyan@gmail.com", "Stoyan", "Mihaylov", null, 0), (2, 2, "ventsi@gmail.com", "Ventsislav", "Draganov", null, 0);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
