# 🏋️ Gym Management System

ระบบจัดการยิมพัฒนาด้วย Java Swing เชื่อมต่อฐานข้อมูล MySQL เหมาะสำหรับใช้งานจริงในธุรกิจยิมขนาดเล็กถึงกลาง รองรับการจัดการสมาชิก, การชำระเงิน, การ Check-in/Check-out, Dashboard สรุปรายงาน และระบบตั้งค่า

---

## ✨ Features

| ฟีเจอร์ | รายละเอียด |
|---|---|
| 🔐 Login | ระบบล็อกอินสำหรับผู้ดูแล พร้อม animation fade-in |
| 📊 Dashboard | แสดงสถิติสมาชิก, รายได้, และ capacity ของยิมแบบ real-time |
| ✅ Check-in / Check-out | บันทึกเวลาเข้า-ออกของสมาชิกพร้อม log รายวัน |
| 💰 Cashier | รับชำระค่าสมาชิก, คำนวณทอน, ออกใบเสร็จ PDF อัตโนมัติ |
| 👥 Member | เพิ่ม/แก้ไข/ลบสมาชิก, กำหนดแพ็กเกจ, ดูสถานะ Active/Expired |
| ⚙️ Settings | ตั้งค่า capacity สูงสุดของยิม |
| 🖨️ Receipt PDF | สร้างใบเสร็จ PDF โดยอัตโนมัติหลังชำระเงินสำเร็จ |

---

## 🗂️ Project Structure

```
Gym-Management-System/
├── Font/                          # Poppins font ทุก weight (TTF)
├── addon/                         # ไฟล์เสริม
│   ├── gym_db.sql                 # SQL Schema + ข้อมูลตัวอย่าง (import ก่อนใช้งาน)
│   ├── flatlaf-3.5.4.jar          # FlatLaf Look & Feel library
│   ├── gradient-icon-font.jar     # Icon font library
│   ├── itext-5.0.5.jar/           # iText PDF library
│   ├── java-swing-timepicker-main/# Time Picker component
│   ├── jcalendar-1.4/             # JCalendar date picker
│   ├── miglayout-4.0.jar          # MigLayout layout manager
│   ├── mysql-connector-j-9.2.0.jar# MySQL JDBC Driver
│   └── FlatLaf-main/              # FlatLaf source/extras
└── NetBeansProjects/
    └── JavaApplication2/          # โปรเจ็คหลัก NetBeans
        ├── src/
        │   ├── javaapplication2/
        │   │   ├── Main.java          # หน้าต่างหลักของแอปพลิเคชัน
        │   │   ├── login.java         # หน้า Login
        │   │   └── DatabaseConnection.java  # การเชื่อมต่อฐานข้อมูล
        │   └── com/raven/
        │       ├── component/         # Menu, Card, Header components
        │       ├── form/              # Dashboard, Cashier, Member, Check-in, Setting
        │       ├── model/             # Data models
        │       ├── event/             # Event listeners
        │       ├── icon/              # Icon resources
        │       └── swing/             # Custom Swing components
        ├── build/                 # ⚠️ ไฟล์ที่ compile แล้ว (ไม่ควร commit)
        ├── dist/                  # ⚠️ ไฟล์ .jar ที่ build แล้ว (ไม่ควร commit)
        ├── receipts/              # ⚠️ ใบเสร็จ PDF ที่สร้างขึ้นขณะใช้งาน (ไม่ควร commit)
        ├── nbproject/private/     # ⚠️ NetBeans private config (ไม่ควร commit)
        ├── build.xml              # Ant build script
        └── manifest.mf            # JAR manifest
```

---

## 🗄️ Database Schema

ใช้ **MySQL / MariaDB** ฐานข้อมูลชื่อ `gym_db` ประกอบด้วย 6 ตาราง

| ตาราง | รายละเอียด |
|---|---|
| `member` | ข้อมูลสมาชิก (ชื่อ, เพศ, เบอร์โทร, email, วันหมดอายุ, สถานะ) |
| `package` | แพ็กเกจสมาชิก (Daily 60฿ / Monthly 2,700฿ / Yearly 30,000฿) |
| `payment` | บันทึกการชำระเงิน (จำนวน, ทอน, วิธีชำระ, path ใบเสร็จ) |
| `checkin_log` | บันทึก Check-in / Check-out ของสมาชิก |
| `gym_setting` | ค่าตั้งต้น (max_capacity, current_capacity) |
| `user` | บัญชีผู้ดูแลระบบ |
| `revenue_report` | รายงานรายได้ (daily/monthly/yearly) |

---

## 🚀 Getting Started

### Prerequisites

- **Java JDK 11+**
- **NetBeans IDE** (แนะนำ 17+)
- **MySQL / MariaDB** (แนะนำผ่าน XAMPP หรือ WampServer)

### 1. ติดตั้งฐานข้อมูล

```sql
-- สร้างฐานข้อมูล
CREATE DATABASE gym_db;
```

จากนั้น import schema ผ่าน phpMyAdmin หรือ MySQL CLI:

```bash
mysql -u root -p gym_db < addon/gym_db.sql
```

### 2. ตั้งค่าการเชื่อมต่อฐานข้อมูล

แก้ไขไฟล์ `NetBeansProjects/JavaApplication2/src/javaapplication2/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/gym_db";
private static final String USER = "root";       // แก้ไข username ของคุณ
private static final String PASSWORD = "";        // แก้ไข password ของคุณ
```

### 3. เพิ่ม Libraries เข้า NetBeans

เปิด NetBeans → คลิกขวาที่โปรเจ็ค → **Properties** → **Libraries** → เพิ่ม JAR ต่อไปนี้จากโฟลเดอร์ `addon/`:

| JAR | หน้าที่ |
|---|---|
| `flatlaf-3.5.4.jar` | Modern UI Look & Feel |
| `mysql-connector-j-9.2.0.jar` | MySQL JDBC Driver |
| `itext-5.0.5.jar` | สร้างใบเสร็จ PDF |
| `miglayout-4.0.jar` | Layout Manager |
| `gradient-icon-font.jar` | Icon fonts |
| `jcalendar-1.4/` | Date Picker |
| `java-swing-timepicker-main/` | Time Picker |

### 4. Run โปรเจ็ค

กด **F6** หรือ **Run → Run Project** ใน NetBeans

### 5. เข้าสู่ระบบ

| Field | ค่าเริ่มต้น |
|---|---|
| Username | `admin` |
| Password | `1234` |

> ⚠️ **คำเตือน**: ควรเปลี่ยน password หลังจาก deploy ใช้งานจริง

---

## 🛠️ Tech Stack

| Technology | รายละเอียด |
|---|---|
| **Java Swing** | GUI Framework หลัก |
| **FlatLaf 3.5.4** | Modern Look & Feel |
| **MySQL / MariaDB** | ฐานข้อมูล |
| **MySQL Connector/J 9.2.0** | JDBC Driver |
| **iText 5.0.5** | สร้าง PDF ใบเสร็จ |
| **MigLayout 4.0** | Layout Manager |
| **JCalendar 1.4** | Date Picker Component |
| **Java Swing TimePicker** | Time Picker Component |
| **Poppins Font** | Typography หลักของ UI |
| **Apache Ant** | Build tool (build.xml) |

---

## 📋 License

โปรเจ็คนี้เป็น Academic Project สำหรับการเรียนการสอน วิชา Java OOP
