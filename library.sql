-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 29, 2024 at 05:16 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `library`
--

-- --------------------------------------------------------

--
-- Table structure for table `authors`
--

CREATE TABLE `authors` (
  `authorId` int(11) NOT NULL,
  `authorName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isDelete` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `authors`
--

INSERT INTO `authors` (`authorId`, `authorName`, `isDelete`) VALUES
(1, 'Nguyễn Nhật Ánh', 0),
(2, 'Tô Hoài', 0),
(3, 'Nguyễn Du', 0),
(4, 'Nam Cao', 0),
(5, 'Xuân Quỳnh', 0),
(8, 'Charlotte Bronte', 0),
(9, 'George Orwell', 0),
(10, 'new author', 0),
(11, 'Robert C. Martin', 0),
(12, 'Ian Goodfellow', 0),
(13, 'Dan Ariely', 0),
(14, 'Carmen M. Latterell,Janelle L. Wilson', 0),
(15, 'Matthias Dehmer,Frank Emmert-Streib,Zengqiang Chen,Xueliang Li,Yongtang Shi', 0),
(16, 'Siddhartha Bhattacharyya,Vaclav Snasel,Aboul Ella Hassanien,Satadal Saha,B. K. Tripathy', 0),
(17, 'Christian Brecher', 0),
(18, 'Nguyễn Quốc Tuấn', 0),
(19, 'N/A', 0);

-- --------------------------------------------------------

--
-- Table structure for table `books`
--

CREATE TABLE `books` (
  `bookId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `authorId` int(11) DEFAULT NULL,
  `categoryId` int(11) DEFAULT NULL,
  `bookName` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `publishDate` date DEFAULT NULL,
  `isDelete` tinyint(1) DEFAULT 0,
  `isbn` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `books`
--

INSERT INTO `books` (`bookId`, `authorId`, `categoryId`, `bookName`, `quantity`, `publishDate`, `isDelete`, `isbn`) VALUES
('B02A11', 14, 13, 'Mathematical Metaphors, Memories, and Mindsets', 12, '2020-04-10', 0, '9781475853483'),
('B1TEDV', 1, 2, 'Kính Vạn Hoa', 10, '1995-01-01', 0, NULL),
('B26241', 18, 13, 'Đường kính và liên hệ dậy cung của đường tròn- Chương II', 9, '2022-01-24', 0, 'N/A'),
('B33FSB', 9, 4, 'Pride and Prejudice', 20, '1941-01-01', 0, NULL),
('B3JFFD', 3, 3, 'Truyện Kiều', 14, '1820-01-01', 0, NULL),
('B7C507', 12, 10, 'Deep Learning', 0, '2016-11-18', 0, '9780262035613'),
('BE0200', 16, 10, 'Deep Learning', 10, '2020-06-22', 0, '9783110670929'),
('BFHDBC', 2, 1, 'Dế mèn phiêu lưu ký', 0, '2004-11-17', 0, '9786042125345');

-- --------------------------------------------------------

--
-- Table structure for table `borrow`
--

CREATE TABLE `borrow` (
  `borrowId` int(11) NOT NULL,
  `bookId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `readerId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `borrowDate` date DEFAULT NULL,
  `returnDate` date DEFAULT NULL,
  `dueDate` date DEFAULT NULL COMMENT 'ngay tra',
  `isDelete` tinyint(1) DEFAULT 0,
  `status` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `borrow`
--

INSERT INTO `borrow` (`borrowId`, `bookId`, `readerId`, `borrowDate`, `returnDate`, `dueDate`, `isDelete`, `status`) VALUES
(74, 'BFHDBC', 'R9A4DE', '2024-08-22', '2024-08-10', NULL, 0, NULL),
(76, 'B3JFFD', 'R71D12', '2024-08-22', '2024-08-27', NULL, 0, NULL),
(77, 'B33FSB', 'R71D12', '2024-08-22', '2024-08-27', '2024-08-22', 0, NULL),
(86, 'BE0200', 'R47EBC', '2024-11-28', '2024-11-29', '2024-11-29', 0, NULL),
(91, 'B26241', 'R47EBC', '2024-11-29', '2024-12-02', NULL, 0, NULL),
(92, 'BFHDBC', 'R47EBC', '2024-11-29', '2024-11-30', NULL, 0, NULL),
(93, 'B7C507', 'R47EBC', '2024-11-29', '2024-12-04', NULL, 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `categoryId` int(11) NOT NULL,
  `categoryName` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`categoryId`, `categoryName`) VALUES
(1, 'Văn học'),
(2, 'Thiếu nhi'),
(3, 'Cổ điển'),
(4, 'Truyện ngắn'),
(5, 'Thơ'),
(6, 'Thần thoại'),
(8, 'Văn học hiện thực'),
(9, 'new category'),
(10, 'Computers'),
(11, 'N/A'),
(12, 'Self-Help'),
(13, 'Education'),
(14, 'Medical');

-- --------------------------------------------------------

--
-- Table structure for table `readers`
--

CREATE TABLE `readers` (
  `readerId` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `readerName` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `readerEmail` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `readerPhoneNumber` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `readerDob` date DEFAULT NULL,
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `isBlock` tinyint(1) DEFAULT 0,
  `userId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `readers`
--

INSERT INTO `readers` (`readerId`, `readerName`, `readerEmail`, `readerPhoneNumber`, `readerDob`, `address`, `isBlock`, `userId`) VALUES
('R47EBC', 'Bùi Minh Huy', 'minhhuybui044@gmail.com', '0387649034', '2004-10-07', 'Hải Dương', 0, 26),
('R6FD8A', 'Đường Văn Long', 'test123@gmail.com', '0123456789', '2004-11-01', 'Hà Nội', 0, 28),
('R71D12', 'Normal Reader', 'taducduy2003@gmail.com', '0948448484', '2024-08-14', 'Ha Noi', 0, 25),
('R9A4DE', 'Reader Late', 'test456@gmail.com', '0948448483', '2024-08-14', 'Ha Noi', 0, 24),
('RHDB32', 'Librarian', 'prohacker035@gmail.com', '0948447372', '2024-08-22', 'Ha Noi', 0, 21);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `userId` int(11) NOT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(1000) DEFAULT NULL,
  `role` varchar(10) DEFAULT 'reader'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`userId`, `username`, `password`, `role`) VALUES
(21, 'librarian', 'c7d25da8e8bc6448012174c120e5b41594152df612d3ae46de5fd9606f0841ea', 'librarian'),
(24, 'latereader', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'reader'),
(25, 'normalreader', '6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b', 'reader'),
(26, 'buihuy2004', 'c7d25da8e8bc6448012174c120e5b41594152df612d3ae46de5fd9606f0841ea', 'reader'),
(28, 'duonglong123', 'c7d25da8e8bc6448012174c120e5b41594152df612d3ae46de5fd9606f0841ea', 'reader');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `authors`
--
ALTER TABLE `authors`
  ADD PRIMARY KEY (`authorId`);

--
-- Indexes for table `books`
--
ALTER TABLE `books`
  ADD PRIMARY KEY (`bookId`),
  ADD KEY `fk_authorId` (`authorId`),
  ADD KEY `fk_categoryId` (`categoryId`);

--
-- Indexes for table `borrow`
--
ALTER TABLE `borrow`
  ADD PRIMARY KEY (`borrowId`),
  ADD KEY `fk_bookId` (`bookId`),
  ADD KEY `fk_readerId` (`readerId`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`categoryId`);

--
-- Indexes for table `readers`
--
ALTER TABLE `readers`
  ADD PRIMARY KEY (`readerId`),
  ADD KEY `readers_users_userId_fk` (`userId`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`userId`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `authors`
--
ALTER TABLE `authors`
  MODIFY `authorId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=20;

--
-- AUTO_INCREMENT for table `borrow`
--
ALTER TABLE `borrow`
  MODIFY `borrowId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=94;

--
-- AUTO_INCREMENT for table `categories`
--
ALTER TABLE `categories`
  MODIFY `categoryId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `userId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `books`
--
ALTER TABLE `books`
  ADD CONSTRAINT `fk_authorId` FOREIGN KEY (`authorId`) REFERENCES `authors` (`authorId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_categoryId` FOREIGN KEY (`categoryId`) REFERENCES `categories` (`categoryId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `borrow`
--
ALTER TABLE `borrow`
  ADD CONSTRAINT `fk_bookId` FOREIGN KEY (`bookId`) REFERENCES `books` (`bookId`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_readerId` FOREIGN KEY (`readerId`) REFERENCES `readers` (`readerId`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `readers`
--
ALTER TABLE `readers`
  ADD CONSTRAINT `readers_users_userId_fk` FOREIGN KEY (`userId`) REFERENCES `users` (`userId`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
