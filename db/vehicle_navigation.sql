USE [master]
GO
/****** Object:  Database [vehicle_navigation]    Script Date: 2/28/2016 7:10:57 PM ******/
CREATE DATABASE [vehicle_navigation]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'verhicle_navigation', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\verhicle_navigation.mdf' , SIZE = 4096KB , MAXSIZE = UNLIMITED, FILEGROWTH = 1024KB )
 LOG ON 
( NAME = N'verhicle_navigation_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL12.MSSQLSERVER\MSSQL\DATA\verhicle_navigation_log.ldf' , SIZE = 1024KB , MAXSIZE = 2048GB , FILEGROWTH = 10%)
GO
ALTER DATABASE [vehicle_navigation] SET COMPATIBILITY_LEVEL = 120
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [vehicle_navigation].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [vehicle_navigation] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [vehicle_navigation] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [vehicle_navigation] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [vehicle_navigation] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [vehicle_navigation] SET ARITHABORT OFF 
GO
ALTER DATABASE [vehicle_navigation] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [vehicle_navigation] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [vehicle_navigation] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [vehicle_navigation] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [vehicle_navigation] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [vehicle_navigation] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [vehicle_navigation] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [vehicle_navigation] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [vehicle_navigation] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [vehicle_navigation] SET  DISABLE_BROKER 
GO
ALTER DATABASE [vehicle_navigation] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [vehicle_navigation] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [vehicle_navigation] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [vehicle_navigation] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [vehicle_navigation] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [vehicle_navigation] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [vehicle_navigation] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [vehicle_navigation] SET RECOVERY FULL 
GO
ALTER DATABASE [vehicle_navigation] SET  MULTI_USER 
GO
ALTER DATABASE [vehicle_navigation] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [vehicle_navigation] SET DB_CHAINING OFF 
GO
ALTER DATABASE [vehicle_navigation] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [vehicle_navigation] SET TARGET_RECOVERY_TIME = 0 SECONDS 
GO
ALTER DATABASE [vehicle_navigation] SET DELAYED_DURABILITY = DISABLED 
GO
EXEC sys.sp_db_vardecimal_storage_format N'vehicle_navigation', N'ON'
GO
USE [vehicle_navigation]
GO
/****** Object:  Table [dbo].[ride_detail]    Script Date: 2/28/2016 7:10:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ride_detail](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[passengerID] [int] NOT NULL,
	[driverID] [int] NOT NULL,
	[from_destination] [nvarchar](150) NOT NULL,
	[to_destination] [nvarchar](150) NOT NULL,
	[from_lat] [float] NOT NULL,
	[from_lng] [float] NOT NULL,
	[to_lat] [float] NOT NULL,
	[to_lng] [float] NOT NULL,
	[reserve_date_start] [datetime] NULL,
	[reserve_date_end] [datetime] NULL,
	[created_date] [datetime] NULL,
	[total_miles] [int] NULL,
	[amount] [float] NULL,
	[status] [int] NOT NULL,
	[review] [nvarchar](150) NULL,
	[rating] [float] NULL,
 CONSTRAINT [PK_ride_detail] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[users]    Script Date: 2/28/2016 7:10:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](100) NOT NULL,
	[email] [nvarchar](100) NOT NULL,
	[password] [nvarchar](150) NOT NULL,
	[phone] [nvarchar](30) NULL,
	[nic] [nvarchar](30) NULL,
	[userType] [nvarchar](20) NULL,
	[street] [nvarchar](50) NULL,
	[city] [nvarchar](50) NULL,
	[country] [nvarchar](50) NULL,
	[lat] [float] NULL,
	[lng] [float] NULL,
	[is_login] [int] NULL,
	[is_vehicle_added] [int] NULL,
	[reg_id] [nvarchar](255) NULL,
	[is_available] [int] NULL,
	[current_lat] [float] NULL,
	[current_lgn] [float] NULL,
	[image] [text] NULL,
	[avg_rating] [float] NULL,
 CONSTRAINT [PK__users__3213E83F3E861C1B] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[vehicle]    Script Date: 2/28/2016 7:10:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[vehicle](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [nvarchar](100) NULL,
	[model_name] [nvarchar](100) NULL,
	[manufacturer_name] [nvarchar](100) NULL,
	[year_name] [int] NULL,
	[ownerId] [int] NULL CONSTRAINT [DF__vehicle__userId__1367E606]  DEFAULT ((0)),
 CONSTRAINT [PK__vehicle__3213E83F12EE0EB8] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
ALTER TABLE [dbo].[vehicle]  WITH CHECK ADD  CONSTRAINT [FK__vehicle__userId__1273C1CD] FOREIGN KEY([ownerId])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[vehicle] CHECK CONSTRAINT [FK__vehicle__userId__1273C1CD]
GO
/****** Object:  StoredProcedure [dbo].[GetNearestDrivers]    Script Date: 2/28/2016 7:10:58 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
CREATE PROCEDURE [dbo].[GetNearestDrivers] 
	-- Add the parameters for the stored procedure here
	@orig_lat decimal(6,2), 
	@orig_long decimal(6,2), 
	@bounding_distance int

AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	-- in meters 1.1515
	-- in km 1.609344
	-- in miles 0.8684
	SET NOCOUNT ON;

 SELECT *,((ACOS(SIN(@orig_lat * PI() / 180) * SIN(lat * PI() / 180) + 
COS(@orig_lat * PI() / 180) * COS(lat * PI() / 180) * COS((@orig_long - lng) * PI() / 180)) * 180 / PI()) * 60 * 1.1515) AS 'distance' 
FROM users
WHERE
(
 (select ((ACOS(SIN(@orig_lat * PI() / 180) * SIN(lat * PI() / 180) + 
COS(@orig_lat * PI() / 180) * COS(lat * PI() / 180) * COS((@orig_long - lng) * PI() / 180)) * 180 / PI()) * 60 * 1.1515)) <= @bounding_distance

) AND userType='Driver' AND is_login = 1 --AND is_available = 1
ORDER BY 'distance' ASC 
END

GO
USE [master]
GO
ALTER DATABASE [vehicle_navigation] SET  READ_WRITE 
GO
