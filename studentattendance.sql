-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 11, 2021 at 09:38 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.2.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `studentattendance`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `accountId` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `teacherCode` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`accountId`, `username`, `password`, `teacherCode`) VALUES
(1, 'lat', '1', 'LAT065377'),
(2, 'abc', '1', 'LAT065377');

-- --------------------------------------------------------

--
-- Table structure for table `attendancesession`
--

CREATE TABLE `attendancesession` (
  `attendanceSessionId` int(11) NOT NULL,
  `numberSession` int(11) NOT NULL,
  `classroomId` int(11) NOT NULL,
  `attendanceDate` date NOT NULL DEFAULT now()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `attendancesessiondetails`
--

CREATE TABLE `attendancesessiondetails` (
  `attendanceSessionId` int(11) NOT NULL,
  `studentCode` varchar(10) NOT NULL,
  `timeCheckIn` datetime NOT NULL DEFAULT current_timestamp(),
  `base64Image` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `class`
--

CREATE TABLE `class` (
  `classCode` varchar(10) NOT NULL,
  `className` varchar(100) NOT NULL,
  `facultyCode` varchar(5) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `class`
--

INSERT INTO `class` (`classCode`, `className`, `facultyCode`) VALUES
('20DTHC3', 'Lớp 20DTHC3 Khóa 20', 'TH'),
('20DTHE3', 'Lớp 20DTHE3 Khóa 20', 'TH'),
('20DTCA2', 'Lớp 20DTCA2 Khóa 20', 'TC');

-- --------------------------------------------------------

--
-- Table structure for table `classroom`
--

CREATE TABLE `classroom` (
  `classroomId` int(11) NOT NULL,
  `classroomName` varchar(100) CHARACTER SET utf8 NOT NULL,
  `createDate` date NOT NULL DEFAULT current_timestamp(),
  `teacherCode` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `classroom`
--

INSERT INTO `classroom` (`classroomId`, `classroomName`, `createDate`, `teacherCode`) VALUES
(2, 'Đồ án Java', '2023-05-15', 'LAT065377');

-- --------------------------------------------------------

--
-- Table structure for table `shedule`
--

CREATE TABLE `shedule` (
  `classroomId` int(11) NOT NULL,
  `studentCode` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `shedule`
--

INSERT INTO `shedule` (`classroomId`, `studentCode`) VALUES
(2, '2011065377'),
(2, '2011065343');

-- --------------------------------------------------------

--
-- Table structure for table `faculty`
--

CREATE TABLE `faculty` (
  `facultyCode` varchar(5) NOT NULL,
  `facultyName` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `faculty`
--

INSERT INTO `faculty` (`facultyCode`, `facultyName`) VALUES
('HT', 'Hệ Thống Thông Tin'),
('TC', 'Tài Chính - Thương Mại'),
('TH', 'Công Nghệ Thông Tin');

-- --------------------------------------------------------

--
-- Table structure for table `student`
--

CREATE TABLE `student` (
  `studentCode` varchar(10) NOT NULL,
  `fullName` varchar(100) CHARACTER SET utf8 NOT NULL,
  `dateOfBirth` date NOT NULL,
  `address` varchar(100) CHARACTER SET utf8 NOT NULL,
  `classCode` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `student`
--

INSERT INTO `student` (`studentCode`, `fullName`, `dateOfBirth`, `address`, `classCode`) VALUES
('2011065377', 'Lê Anh Tuấn', '2000-12-25', 'abc, Quận 9, TP. HCM', '20DTHC3'),
('2011065343', 'Nguyễn Phạm Hữu Dự', '2000-08-02', 'ghi, Quận KL, TP. HCM', '20DTHE3');


-- --------------------------------------------------------

--
-- Table structure for table `teacher`
--

CREATE TABLE `teacher` (
  `teacherCode` varchar(10) NOT NULL,
  `fullName` varchar(100) CHARACTER SET utf8 NOT NULL,
  `email` varchar(100) CHARACTER SET utf8 NOT NULL,
  `password` varchar(100) NOT NULL,
  `phoneNumber` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `teacher`
--

INSERT INTO `teacher` (`teacherCode`, `fullName`, `email`, `password`, `phoneNumber`) VALUES
('LAT065377', 'LAT', 'anhtuan122536@gmail.com', '1', '0343405727');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`accountId`),
  ADD UNIQUE KEY `account_username_uindex` (`username`),
  ADD KEY `account_teacher_teacherCode_fk` (`teacherCode`);

--
-- Indexes for table `attendancesession`
--
ALTER TABLE `attendancesession`
  ADD PRIMARY KEY (`attendanceSessionId`,`numberSession`),
  ADD KEY `attendanceSession_classroom_classroomId_fk` (`classroomId`);

--
-- Indexes for table `attendancesessiondetails`
--
ALTER TABLE `attendancesessiondetails`
  ADD KEY `attendanceSessionDetails_attendanceSessionId_fk` (`attendanceSessionId`),
  ADD KEY `attendanceSessionDetails_student_studentCode_fk` (`studentCode`);

--
-- Indexes for table `class`
--
ALTER TABLE `class`
  ADD PRIMARY KEY (`classCode`),
  ADD KEY `class_faculty_facultyCode_fk` (`facultyCode`);

--
-- Indexes for table `classroom`
--
ALTER TABLE `classroom`
  ADD PRIMARY KEY (`classroomId`),
  ADD KEY `classroom_teacher_teacherCode_fk` (`teacherCode`);

--
-- Indexes for table `shedule`
--
ALTER TABLE `shedule`
  ADD PRIMARY KEY (`classroomId`,`studentCode`),
  ADD KEY `classroom_student_student_studentCode_fk` (`studentCode`);

--
-- Indexes for table `faculty`
--
ALTER TABLE `faculty`
  ADD PRIMARY KEY (`facultyCode`);

--
-- Indexes for table `student`
--
ALTER TABLE `student`
  ADD UNIQUE KEY `student_studentCode_uindex` (`studentCode`),
  ADD KEY `student_class_classCode_fk` (`classCode`);

--
-- Indexes for table `teacher`
--
ALTER TABLE `teacher`
  ADD PRIMARY KEY (`teacherCode`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `accountId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `attendancesession`
--
ALTER TABLE `attendancesession`
  MODIFY `attendanceSessionId` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `classroom`
--
ALTER TABLE `classroom`
  MODIFY `classroomId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `account`
--
ALTER TABLE `account`
  ADD CONSTRAINT `account_teacher_teacherCode_fk` FOREIGN KEY (`teacherCode`) REFERENCES `teacher` (`teacherCode`);

--
-- Constraints for table `attendancesession`
--
ALTER TABLE `attendancesession`
  ADD CONSTRAINT `attendanceSession_classroom_classroomId_fk` FOREIGN KEY (`classroomId`) REFERENCES `classroom` (`classroomId`);

--
-- Constraints for table `attendancesessiondetails`
--
ALTER TABLE `attendancesessiondetails`
  ADD CONSTRAINT `attendanceSessionDetails_attendanceSessionId_fk` FOREIGN KEY (`attendanceSessionId`) REFERENCES `attendancesession` (`attendanceSessionId`),
  ADD CONSTRAINT `attendanceSessionDetails_student_studentCode_fk` FOREIGN KEY (`studentCode`) REFERENCES `student` (`studentCode`);

--
-- Constraints for table `class`
--
ALTER TABLE `class`
  ADD CONSTRAINT `class_faculty_facultyCode_fk` FOREIGN KEY (`facultyCode`) REFERENCES `faculty` (`facultyCode`);

--
-- Constraints for table `classroom`
--
ALTER TABLE `classroom`
  ADD CONSTRAINT `classroom_teacher_teacherCode_fk` FOREIGN KEY (`teacherCode`) REFERENCES `teacher` (`teacherCode`);

--
-- Constraints for table `shedule`
--
ALTER TABLE `shedule`
  ADD CONSTRAINT `classroom_student_classroom_classroomId_fk` FOREIGN KEY (`classroomId`) REFERENCES `classroom` (`classroomId`),
  ADD CONSTRAINT `classroom_student_student_studentCode_fk` FOREIGN KEY (`studentCode`) REFERENCES `student` (`studentCode`);

--
-- Constraints for table `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_class_classCode_fk` FOREIGN KEY (`classCode`) REFERENCES `class` (`classCode`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
