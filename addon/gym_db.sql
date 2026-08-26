-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Mar 27, 2025 at 07:29 PM
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
-- Database: `gym_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `checkin_log`
--

CREATE TABLE `checkin_log` (
  `log_id` int(11) NOT NULL,
  `member_id` int(11) DEFAULT NULL,
  `member_name` varchar(50) NOT NULL,
  `checkin_time` time DEFAULT NULL,
  `checkout_time` time DEFAULT NULL,
  `date` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `checkin_log`
--

INSERT INTO `checkin_log` (`log_id`, `member_id`, `member_name`, `checkin_time`, `checkout_time`, `date`) VALUES
(1, 2, 'Guest', NULL, NULL, '2025-03-10 09:00:00'),
(2, 4, 'Liam', NULL, NULL, '2025-03-10 10:00:00'),
(3, 5, 'Emma', NULL, NULL, '2025-03-10 11:00:00'),
(4, 6, 'Noah', NULL, NULL, '2025-03-10 12:00:00'),
(5, 7, 'Olivia', NULL, NULL, '2025-03-10 13:00:00'),
(6, 8, 'James', NULL, NULL, '2025-03-10 14:00:00'),
(7, 9, 'Sophia', NULL, NULL, '2025-03-10 15:00:00'),
(8, 10, 'William', NULL, NULL, '2025-03-10 16:00:00'),
(9, 11, 'Isabella', NULL, NULL, '2025-03-10 17:00:00'),
(10, 12, 'Ethan', NULL, NULL, '2025-03-10 18:00:00'),
(28, 8, 'James', '00:54:00', '01:16:00', '2025-03-28 00:54:33'),
(29, 8, 'James', '00:54:00', NULL, '2025-03-28 00:54:52'),
(30, 8, 'James', '00:54:00', NULL, '2025-03-28 00:54:55'),
(31, 12, 'Ethan', '01:00:00', NULL, '2025-03-28 01:01:13'),
(32, 12, 'Ethan', '01:04:00', NULL, '2025-03-28 01:04:35'),
(33, 4, 'Liam', '01:07:00', NULL, '2025-03-28 01:08:47');

-- --------------------------------------------------------

--
-- Table structure for table `gym_setting`
--

CREATE TABLE `gym_setting` (
  `setting_id` int(11) NOT NULL,
  `max_capacity` int(11) DEFAULT NULL,
  `current_capacity` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `gym_setting`
--

INSERT INTO `gym_setting` (`setting_id`, `max_capacity`, `current_capacity`) VALUES
(1, 50, 5);

-- --------------------------------------------------------

--
-- Table structure for table `member`
--

CREATE TABLE `member` (
  `member_id` int(11) NOT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `join_date` date DEFAULT NULL,
  `expire_date` date DEFAULT NULL,
  `package_id` int(11) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `member`
--

INSERT INTO `member` (`member_id`, `firstname`, `lastname`, `gender`, `phone`, `email`, `join_date`, `expire_date`, `package_id`, `status`) VALUES
(2, 'Guest', 'Member', '-', '-', '-', '2024-12-29', '2024-12-29', 1, 'Active'),
(4, 'Liam', 'Smith', 'Male', '+314-5692345', 'liam.smith@example.com', '2021-06-12', '2023-08-14', 2, 'Active'),
(5, 'Emma', 'Johnson', 'Female', '+402-9876543', 'emma.johnson@example.com', '2019-12-29', '2023-12-31', 2, 'Active'),
(6, 'Noah', 'Brown', 'Male', '+523-7654321', 'noah.brown@example.com', '2022-01-10', '2025-05-09', 2, 'Active'),
(7, 'Olivia', 'Williams', 'Female', '+698-3456789', 'olivia.williams@example.com', '2023-01-01', '2023-12-31', 2, 'Active'),
(8, 'James', 'Taylor', 'Male', '+785-1239876', 'james.taylor@example.com', '2021-11-29', '2023-07-30', 3, 'Active'),
(9, 'Sophia', 'Anderson', 'Female', '+876-5432198', 'sophia.anderson@example.com', '2020-05-18', '2022-09-22', 3, 'Expired'),
(10, 'William', 'Martinez', 'Male', '+432-9081723', 'william.martinez@example.com', '2022-12-05', '2024-06-18', 2, 'Active'),
(11, 'Isabella', 'Garcia', 'Female', '+312-7584932', 'isabella.garcia@example.com', '2023-07-19', '2024-11-29', 3, 'Expired'),
(12, 'Ethan', 'Rodriguez', 'Male', '+542-8675349', 'ethan.rodriguez@example.com', '2021-03-22', '2023-10-05', 3, 'Active'),
(13, 'Mia', 'Harriss', 'Female', '+689-2345678', 'mia.harris@example.com', '2019-12-29', '2021-12-26', 2, 'Expired'),
(14, 'test', 'dddd', 'Male', '5555', 'yyyy', '2024-12-29', '2024-12-29', 2, 'Active'),
(16, 'jay', 'test', 'Male', '0896141138', 'zazajayzaza123@gmail.com', '2024-12-29', '2024-12-29', 2, 'Active'),
(17, 'jay', 'test', 'Male', '0896141138', 'zazajayzaza123@gmail.com', '2023-12-31', '2024-12-29', 2, 'Active'),
(18, 'you', 'you', 'Male', '8888', 'gg@gmail.com', '2024-12-29', '2023-12-31', 2, 'Active'),
(19, 'jaw', 'qa', 'Male', '777', 'EZ', '2024-12-29', '2024-12-29', 3, 'Active'),
(20, 'hhhh', 'hhhh', 'Female', 'hhhh', 'hhh', '2024-12-29', '2024-12-29', 2, 'Active');

-- --------------------------------------------------------

--
-- Table structure for table `package`
--

CREATE TABLE `package` (
  `package_id` int(11) NOT NULL,
  `package_name` varchar(50) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `package`
--

INSERT INTO `package` (`package_id`, `package_name`, `duration`, `price`) VALUES
(1, 'Daily', 1, 60.00),
(2, 'Monthly', 30, 2700.00),
(3, 'Yearly', 365, 30000.00);

-- --------------------------------------------------------

--
-- Table structure for table `payment`
--

CREATE TABLE `payment` (
  `payment_id` int(11) NOT NULL,
  `member_id` int(11) DEFAULT NULL,
  `package_id` int(11) DEFAULT NULL,
  `payment_date` datetime DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `amount_received` decimal(10,2) NOT NULL,
  `amount_change` decimal(10,2) NOT NULL,
  `payment_method` varchar(20) DEFAULT NULL,
  `receipt_pdf_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `payment`
--

INSERT INTO `payment` (`payment_id`, `member_id`, `package_id`, `payment_date`, `amount`, `amount_received`, `amount_change`, `payment_method`, `receipt_pdf_path`) VALUES
(11, 2, 1, '2025-03-12 18:41:46', 107.00, 110.00, 3.00, 'Mobile Banking', 'D:\\Work KUMTNB\\java oop\\project\\code\\JAVA\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_11.pdf'),
(12, 2, 1, '2025-03-23 17:51:19', 422.65, 30000.00, 29577.35, 'Cash', 'C:\\Users\\zazaj\\Downloads\\JAVA-7 (1)\\JAVA-7\\NetBeansProjects\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_12.pdf'),
(13, 2, 1, '2025-03-23 17:52:06', 1000.45, 3000.00, 1999.55, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_13.pdf'),
(14, 2, 1, '2025-03-23 18:23:04', 957.65, 20000.00, 19042.35, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_14.pdf'),
(15, 2, 1, '2025-03-23 18:33:08', 2568.00, 3000.00, 432.00, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_15.pdf'),
(16, 2, 1, '2025-03-23 18:35:55', 957.65, 30000.00, 29042.35, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_16.pdf'),
(17, 4, 2, '2025-03-24 00:07:05', 856.00, 2000.00, 1144.00, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_17.pdf'),
(18, 5, 2, '2025-03-24 20:30:16', 957.65, 1000.00, 42.35, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_18.pdf'),
(19, 2, 1, '2025-03-24 20:34:09', 1401.70, 3000.00, 1598.30, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_19.pdf'),
(20, 2, 1, '2025-03-27 23:18:35', 1401.70, 40000.00, 38598.30, 'Cash', 'E:\\JAVA-6\\NetBeansProjects\\JavaApplication2\\receipts\\receipt_20.pdf');

-- --------------------------------------------------------

--
-- Table structure for table `revenue_report`
--

CREATE TABLE `revenue_report` (
  `report_id` int(11) NOT NULL,
  `report_date` date DEFAULT NULL,
  `daily_income` decimal(10,2) DEFAULT NULL,
  `monthly_income` decimal(10,2) DEFAULT NULL,
  `yearly_income` decimal(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `user_name`, `user_password`) VALUES
(1, 'admin', '1234');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `checkin_log`
--
ALTER TABLE `checkin_log`
  ADD PRIMARY KEY (`log_id`),
  ADD KEY `member_id` (`member_id`);

--
-- Indexes for table `gym_setting`
--
ALTER TABLE `gym_setting`
  ADD PRIMARY KEY (`setting_id`);

--
-- Indexes for table `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`member_id`),
  ADD KEY `member_id` (`package_id`);

--
-- Indexes for table `package`
--
ALTER TABLE `package`
  ADD PRIMARY KEY (`package_id`);

--
-- Indexes for table `payment`
--
ALTER TABLE `payment`
  ADD PRIMARY KEY (`payment_id`),
  ADD KEY `member_id` (`member_id`),
  ADD KEY `package_id` (`package_id`);

--
-- Indexes for table `revenue_report`
--
ALTER TABLE `revenue_report`
  ADD PRIMARY KEY (`report_id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `checkin_log`
--
ALTER TABLE `checkin_log`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT for table `member`
--
ALTER TABLE `member`
  MODIFY `member_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `package`
--
ALTER TABLE `package`
  MODIFY `package_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `payment`
--
ALTER TABLE `payment`
  MODIFY `payment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `revenue_report`
--
ALTER TABLE `revenue_report`
  MODIFY `report_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `checkin_log`
--
ALTER TABLE `checkin_log`
  ADD CONSTRAINT `checkin_log_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`);

--
-- Constraints for table `member`
--
ALTER TABLE `member`
  ADD CONSTRAINT `member_id` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`);

--
-- Constraints for table `payment`
--
ALTER TABLE `payment`
  ADD CONSTRAINT `payment_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`),
  ADD CONSTRAINT `payment_ibfk_2` FOREIGN KEY (`package_id`) REFERENCES `package` (`package_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
