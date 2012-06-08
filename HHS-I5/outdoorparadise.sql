SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;


CREATE TABLE IF NOT EXISTS `booking` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `amountOfAdults` smallint(6) NOT NULL,
  `amountOfKids` smallint(6) NOT NULL,
  `hasCancellationInsurance` tinyint(1) NOT NULL,
  `salePrice` decimal(10,0) NOT NULL,
  `private` int(11) NOT NULL,
  `trip` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `private` (`private`),
  KEY `trip` (`trip`),
  KEY `private_2` (`private`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `bookingexcursion` (
  `booking` int(11) NOT NULL,
  `excursion` int(11) NOT NULL,
  `amountOfPeople` int(11) NOT NULL,
  PRIMARY KEY (`booking`,`excursion`),
  KEY `excursion` (`excursion`),
  KEY `booking` (`booking`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `employee` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `socialSecurityNumber` text NOT NULL,
  `email` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=3 ;

CREATE TABLE IF NOT EXISTS `excursion` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `maxAmountOfPeople` int(11) NOT NULL,
  `guide` text NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `excursionType` int(11) NOT NULL,
  `trip` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `trip` (`trip`),
  KEY `excursionType` (`excursionType`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `excursiontype` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `description` text NOT NULL,
  `tripType` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `tripType` (`tripType`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `private` (
  `customer` int(11) NOT NULL,
  `phone` text NOT NULL,
  `firstName` text NOT NULL,
  `surName` text NOT NULL,
  `state` text NOT NULL,
  PRIMARY KEY (`customer`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE IF NOT EXISTS `product` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `productName` text NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `trip` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `endDate` date NOT NULL,
  `startDate` date NOT NULL,
  `tripType` int(11) NOT NULL,
  `price` decimal(10,0) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `tripType` (`tripType`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `triptype` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `name` text NOT NULL,
  `description` text NOT NULL,
  `maxAmountOfPeople` int(11) NOT NULL,
  `kidsAllowed` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

CREATE TABLE IF NOT EXISTS `triptypeproduct` (
  `tripType` int(11) NOT NULL,
  `product` int(11) NOT NULL,
  PRIMARY KEY (`tripType`,`product`),
  KEY `product` (`product`),
  KEY `tripType` (`tripType`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


ALTER TABLE `booking`
  ADD CONSTRAINT `FK_booking_private` FOREIGN KEY (`private`) REFERENCES `private` (`customer`),
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`trip`) REFERENCES `trip` (`ID`),
  ADD CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`private`) REFERENCES `private` (`customer`),
  ADD CONSTRAINT `FK_booking_trip` FOREIGN KEY (`trip`) REFERENCES `trip` (`ID`);

ALTER TABLE `bookingexcursion`
  ADD CONSTRAINT `FK_bookingexcursion_excursion` FOREIGN KEY (`excursion`) REFERENCES `excursion` (`ID`),
  ADD CONSTRAINT `bookingexcursion_ibfk_1` FOREIGN KEY (`booking`) REFERENCES `booking` (`ID`),
  ADD CONSTRAINT `bookingexcursion_ibfk_2` FOREIGN KEY (`excursion`) REFERENCES `excursion` (`ID`),
  ADD CONSTRAINT `FK_bookingexcursion_booking` FOREIGN KEY (`booking`) REFERENCES `booking` (`ID`);

ALTER TABLE `excursion`
  ADD CONSTRAINT `FK_excursion_excursionType` FOREIGN KEY (`excursionType`) REFERENCES `excursiontype` (`ID`),
  ADD CONSTRAINT `excursion_ibfk_1` FOREIGN KEY (`excursionType`) REFERENCES `excursiontype` (`ID`),
  ADD CONSTRAINT `excursion_ibfk_2` FOREIGN KEY (`trip`) REFERENCES `trip` (`ID`),
  ADD CONSTRAINT `FK_excursion_trip` FOREIGN KEY (`trip`) REFERENCES `trip` (`ID`);

ALTER TABLE `excursiontype`
  ADD CONSTRAINT `FK_excursiontype_tripType` FOREIGN KEY (`tripType`) REFERENCES `triptype` (`ID`),
  ADD CONSTRAINT `excursiontype_ibfk_1` FOREIGN KEY (`tripType`) REFERENCES `triptype` (`ID`);

ALTER TABLE `trip`
  ADD CONSTRAINT `FK_trip_tripType` FOREIGN KEY (`tripType`) REFERENCES `triptype` (`ID`),
  ADD CONSTRAINT `trip_ibfk_1` FOREIGN KEY (`tripType`) REFERENCES `triptype` (`ID`);

ALTER TABLE `triptypeproduct`
  ADD CONSTRAINT `FK_triptypeproduct_tripType` FOREIGN KEY (`tripType`) REFERENCES `triptype` (`ID`),
  ADD CONSTRAINT `FK_triptypeproduct_product` FOREIGN KEY (`product`) REFERENCES `product` (`ID`),
  ADD CONSTRAINT `triptypeproduct_ibfk_1` FOREIGN KEY (`tripType`) REFERENCES `triptype` (`ID`),
  ADD CONSTRAINT `triptypeproduct_ibfk_2` FOREIGN KEY (`product`) REFERENCES `product` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
