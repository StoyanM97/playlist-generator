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
  `ArtistId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `ArtistTrackListUrl` VARCHAR(45) NULL,
  PRIMARY KEY (`ArtistId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`albums`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`albums` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`albums` (
  `AlbumId` INT NOT NULL AUTO_INCREMENT,
  `Title` VARCHAR(45) NOT NULL,
  `ArtistId` INT NOT NULL,
  `AlbumTracklistUrl` VARCHAR(45) NULL,
  PRIMARY KEY (`AlbumId`),
  INDEX `album_artist_relation_idx` (`ArtistId` ASC),
  CONSTRAINT `album_artist_relation`
    FOREIGN KEY (`ArtistId`)
    REFERENCES `playlist_generator_db`.`artists` (`ArtistId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`genres`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`genres` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`genres` (
  `GenreId` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(45) NOT NULL,
  `ImageUrl` VARCHAR(300) NULL,
  PRIMARY KEY (`GenreId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`tracks`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`tracks` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`tracks` (
  `TrackId` INT NOT NULL AUTO_INCREMENT,
  `Title` VARCHAR(45) NOT NULL,
  `PreviewUrl` VARCHAR(300) NULL,
  `Duration` INT NOT NULL,
  `Rank` INT NOT NULL,
  `AlbumId` INT NOT NULL,
  `Link` VARCHAR(300) NOT NULL,
  `GenreId` INT NOT NULL,
  PRIMARY KEY (`TrackId`),
  INDEX `track_album_relation_idx` (`AlbumId` ASC),
  INDEX `track_genre_relation_idx` (`GenreId` ASC),
  CONSTRAINT `track_album_relation`
    FOREIGN KEY (`AlbumId`)
    REFERENCES `playlist_generator_db`.`albums` (`AlbumId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `track_genre_relation`
    FOREIGN KEY (`GenreId`)
    REFERENCES `playlist_generator_db`.`genres` (`GenreId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`users` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`users` (
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `enabled` TINYINT(1) NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`user_details`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`user_details` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`user_details` (
  `UserDetailId` INT NOT NULL AUTO_INCREMENT,
  `UserName` VARCHAR(45) NOT NULL,
  `Email` VARCHAR(45) NOT NULL,
  `FirstName` VARCHAR(45) NOT NULL,
  `LastName` VARCHAR(45) NOT NULL,
  `Avatar` LONGBLOB NULL,
  `IsDeleted` TINYINT(1) NULL,
  PRIMARY KEY (`UserDetailId`),
  INDEX `user_details_user_relation_idx` (`UserName` ASC),
  CONSTRAINT `user_details_user_relation`
    FOREIGN KEY (`UserName`)
    REFERENCES `playlist_generator_db`.`users` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`playlists`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`playlists` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`playlists` (
  `PlaylistId` INT NOT NULL AUTO_INCREMENT,
  `Title` VARCHAR(45) NOT NULL,
  `UserId` INT NOT NULL,
  `IsDeleted` TINYINT(1) NULL,
  PRIMARY KEY (`PlaylistId`),
  INDEX `user_playlist_relation_idx` (`UserId` ASC),
  CONSTRAINT `user_playlist_relation`
    FOREIGN KEY (`UserId`)
    REFERENCES `playlist_generator_db`.`user_details` (`UserDetailId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`genre_playlist_relations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`genre_playlist_relations` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`genre_playlist_relations` (
  `GenrePlaylistRelationId` INT NOT NULL AUTO_INCREMENT,
  `PlaylistId` INT NOT NULL,
  `GenreId` INT NOT NULL,
  `IsDeleted` TINYINT(1) NULL,
  PRIMARY KEY (`GenrePlaylistRelationId`),
  INDEX `genre_playlist_relation_idx` (`GenreId` ASC),
  INDEX `playlist_genre_ralation_idx` (`PlaylistId` ASC),
  CONSTRAINT `genre_playlist_relation`
    FOREIGN KEY (`GenreId`)
    REFERENCES `playlist_generator_db`.`genres` (`GenreId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `playlist_genre_ralation`
    FOREIGN KEY (`PlaylistId`)
    REFERENCES `playlist_generator_db`.`playlists` (`PlaylistId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`playlist_track_relations`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`playlist_track_relations` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`playlist_track_relations` (
  `PlaylistTrackRelationId` INT NOT NULL AUTO_INCREMENT,
  `PlaylistId` INT NOT NULL,
  `TrackId` INT NOT NULL,
  `IsDeleted` TINYINT(1) NULL,
  PRIMARY KEY (`PlaylistTrackRelationId`),
  INDEX `playlist_track_relation_idx` (`PlaylistId` ASC),
  INDEX `track_playlist_relation_idx` (`TrackId` ASC),
  CONSTRAINT `playlist_track_relation`
    FOREIGN KEY (`PlaylistId`)
    REFERENCES `playlist_generator_db`.`playlists` (`PlaylistId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `track_playlist_relation`
    FOREIGN KEY (`TrackId`)
    REFERENCES `playlist_generator_db`.`tracks` (`TrackId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `playlist_generator_db`.`authority`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `playlist_generator_db`.`authority` ;

CREATE TABLE IF NOT EXISTS `playlist_generator_db`.`authority` (
  `username` VARCHAR(45) NOT NULL,
  `role_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC),
  CONSTRAINT `user_authority_relation`
    FOREIGN KEY (`username`)
    REFERENCES `playlist_generator_db`.`users` (`username`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
