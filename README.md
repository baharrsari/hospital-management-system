# 🏥 Hospital Management System

A Java & MySQL based desktop hospital management application.

This project is a simple hospital management system that allows managing patients, doctors, and appointments using Java (Swing) and MySQL. It demonstrates JDBC usage, DAO pattern, and a clean project structure suitable for junior-level backend / desktop application development.

---

## 🧾 Features

- Add and view patients  
- Add and view doctors  
- Create and list appointments  
- MySQL database integration via JDBC  
- Layered structure (model, dao, ui)

---

## 📁 Project Structure

```text
hospital-management-system
├── db
│   └── schema.sql
├── src
│   └── main
│       └── java
│           └── com
│               └── hospital
│                   ├── dao
│                   │   ├── AppointmentDAO.java
│                   │   ├── DoctorDAO.java
│                   │   └── PatientDAO.java
│                   ├── db
│                   │   ├── DBConfig.java
│                   │   └── DBConnection.java
│                   ├── model
│                   │   ├── Appointment.java
│                   │   ├── Doctor.java
│                   │   └── Patient.java
│                   ├── ui
│                   │   ├── AppointmentsPanel.java
│                   │   ├── DoctorsPanel.java
│                   │   ├── PatientsPanel.java
│                   │   ├── HomePanel.java
│                   │   └── MainFrame.java
│                   ├── util
│                   │   └── DBTest.java
│                   ├── App.java
│                   └── Main.java
├── pom.xml
└── README.md
```
---

## 🛠 Technologies Used

- Java (Swing, JDBC)
- MySQL
- Maven
- MySQL Connector/J

---

## 📌 Prerequisites

- Java JDK 8 or later
- MySQL Server
- MySQL Workbench or MySQL CLI
- Maven (optional)

---

## 📝 Notes

- Database schema is provided as an SQL script.
- Database credentials are not included in the repository.
- This project is intended for educational purposes.
