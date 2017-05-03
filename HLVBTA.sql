-- MySQL dump 10.13  Distrib 5.7.18, for Linux (x86_64)
--
-- Host: localhost    Database: HLVBTA
-- ------------------------------------------------------
-- Server version	5.7.18-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `AD`
--

DROP TABLE IF EXISTS `AD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AD` (
  `adID` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `devID` int(11) DEFAULT NULL,
  `impressions` varchar(45) DEFAULT NULL,
  `uniqueUsrs` varchar(45) DEFAULT NULL,
  `minSpeed` int(11) DEFAULT NULL,
  `maxSpeed` int(11) DEFAULT NULL,
  `websiteUrl` varchar(128) DEFAULT NULL,
  `Direction` int(1) DEFAULT NULL,
  `startTime` char(10) DEFAULT NULL,
  `endTime` char(10) DEFAULT NULL,
  PRIMARY KEY (`adID`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AD`
--

LOCK TABLES `AD` WRITE;
/*!40000 ALTER TABLE `AD` DISABLE KEYS */;
INSERT INTO `AD` VALUES (1,'Science','Planetarium show today at 5pm in Science Building.',1,'0','0',0,10,'http://www.rowan.edu/home/science-mathematics',1,'0.0','24.0'),(2,'Rec Center','Need to go to the gym?',1,'0','0',0,10,'http://www.rowan.edu/studentaffairs/rec/',2,'0.0','24.0'),(3,'Student Center Front','RA show tonight at 9pm in the SC.',1,'0','0',0,10,'http://www.rowan.edu/studentaffairs/studentcenter/',2,'0.0','24.0'),(4,'Student Center Patio','Back patio party tomorrow at 7pm.',1,'0','0',0,10,'http://www.rowan.edu/studentaffairs/studentcenter/',2,'0.0','24.0'),(5,'Landmark Americana','Half off Blue Moons tonight at 10pm.',1,'0','0',0,10,'http://www.landmarkamericana.com/',2,'20.0','24.0'),(6,'Walgreens','Red solo cups half off.',1,'0','0',0,10,'https://www.walgreens.com/',2,'0.0','19.0'),(7,'Savitz Hall','If you need financial aid come to Savitz.',1,'0','0',0,10,'http://www.rowan.edu/home/undergraduate-admissions',2,'0.0','24.0'),(8,'Savitz Hall','Apply to Rowan University come to Savitz.',1,'0','0',10,100,'http://www.rowan.edu/home/undergraduate-admissions',2,'0.0','24.0'),(9,'Landmark Americana','Turn Left on Delsea Drive for 1/2 off Drinks',1,'0','0',10,100,'http://www.landmarkamericana.com/g-home/',1,'20.0','24.0'),(10,'Landmark Americana','Turn Left on West Street for 1/2 off Drinks',1,'0','0',10,100,'http://www.landmarkamericana.com/g-home/',1,'20.0','24.0'),(11,'Landmark Americana','Park in the lot to the right for 1/2 off Drinks',1,'0','0',15,100,'http://www.landmarkamericana.com/g-home/',1,'20.0','24.0'),(12,'Science','Turn around the Planetarium is behind you.',1,'0','0',0,0,'http://www.rowan.edu/studentaffairs/rec/',2,'0.0','24.0');
/*!40000 ALTER TABLE `AD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Advertiser`
--

DROP TABLE IF EXISTS `Advertiser`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Advertiser` (
  `devID` int(11) DEFAULT NULL,
  `moneyOwned` varchar(45) DEFAULT NULL,
  `totalAdViews` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Advertiser`
--

LOCK TABLES `Advertiser` WRITE;
/*!40000 ALTER TABLE `Advertiser` DISABLE KEYS */;
INSERT INTO `Advertiser` VALUES (1,'200','350'),(2,'0','0'),(3,'0','0'),(4,'0','0'),(5,'0','0'),(6,'0','0');
/*!40000 ALTER TABLE `Advertiser` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Developer`
--

DROP TABLE IF EXISTS `Developer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Developer` (
  `devID` int(11) DEFAULT NULL,
  `impressions` int(11) DEFAULT NULL,
  `numberOfDisplayedADs` int(11) DEFAULT NULL,
  `click` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Developer`
--

LOCK TABLES `Developer` WRITE;
/*!40000 ALTER TABLE `Developer` DISABLE KEYS */;
INSERT INTO `Developer` VALUES (1,0,50,NULL);
/*!40000 ALTER TABLE `Developer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Location`
--

DROP TABLE IF EXISTS `Location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Location` (
  `latitude` varchar(45) DEFAULT NULL,
  `longitude` varchar(45) DEFAULT NULL,
  `minDistance` varchar(32) DEFAULT NULL,
  `maxDistance` varchar(32) DEFAULT NULL,
  `adID` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Location`
--

LOCK TABLES `Location` WRITE;
/*!40000 ALTER TABLE `Location` DISABLE KEYS */;
INSERT INTO `Location` VALUES ('39.709050','-75.119855','0','1',8),('39.709050','-75.119855','0','1',7),('39.707333','-75.109288','0','1',6),('39.706722','-75.110167','0','1',5),('39.709877','-75.120682','0','1',1),('39.710197','—75.118150','0','1',2),('39.708422','-75.117828','0','1',3),('39.708942','-75.118085','0','1',4),('39.705276','-75.107940','0','1',9),('39.707056','-75.108719','0','1',10),('39.706573','-75.110430','0','1',11),('39.709877','-75.120682','0','1',12);
/*!40000 ALTER TABLE `Location` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-03 17:14:49
