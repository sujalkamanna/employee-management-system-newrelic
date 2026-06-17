# Employee Management System with Docker, MySQL, AWS EC2 & New Relic Observability

## Project Overview

This project demonstrates a complete deployment of a Java-based Employee Management System built using JSP, Servlets, JDBC, and MySQL.

The application is containerized using Docker and deployed on an AWS EC2 instance. Application performance and infrastructure metrics are monitored using New Relic APM and New Relic Infrastructure Monitoring.

The application supports:

* User Authentication
* Employee Management
* Create Employee
* View Employee Records
* Update Employee Records
* Delete Employee Records
* MySQL Database Integration
* Dockerized Deployment
* AWS EC2 Hosting
* New Relic Application Monitoring
* New Relic Infrastructure Monitoring

---

# Architecture

```text
User Browser
      |
      v
+--------------------+
| AWS EC2 Instance   |
+--------------------+
      |
      +----------------------+
      |                      |
      v                      v
+-------------+      +-------------+
| Tomcat App  | ---> | MySQL 5.7   |
| Docker      |      | Docker      |
+-------------+      +-------------+
      |
      v
+--------------------+
| New Relic APM      |
+--------------------+

      |

+--------------------+
| New Relic Infra    |
+--------------------+
```

---

# Technology Stack

## Backend

* Java 8
* JSP
* Servlets
* JDBC

## Database

* MySQL 5.7

## Frontend

* HTML
* CSS
* Bootstrap 4

## Build Tool

* Maven

## Containerization

* Docker

## Monitoring

* New Relic APM
* New Relic Infrastructure Monitoring

## Cloud

* AWS EC2 (Ubuntu 24.04)

---

# Repository Structure

```text
employee-management-system-newrelic/

├── README.md
├── .gitignore

├── crud-db-app/
│   ├── src/
│   ├── WebContent/
│   ├── database/
│   │   ├── 01-schema.sql
│   │   └── 02-seed-data.sql
│   ├── Dockerfile
│   └── pom.xml

├── monitoring/
│   └── newrelic/
│       └── newrelic-infra.yml

└── docs/
    └── screenshots/
```

---

# AWS EC2 Setup

## Launch Instance

Recommended:

* Ubuntu Server 24.04 LTS
* t2.medium or larger
* 20 GB Storage

Security Group:

| Port | Protocol | Purpose          |
| ---- | -------- | ---------------- |
| 22   | TCP      | SSH              |
| 8080 | TCP      | Application      |
| 3306 | TCP      | MySQL (Optional) |

---

# Connect to EC2

```bash
ssh -i key.pem ubuntu@PUBLIC_IP
```

or

```bash
ssh -i key.pem root@PUBLIC_IP
```

---

# Install Required Packages

Update System

```bash
apt update && apt upgrade -y
```

Install Utilities

```bash
apt install -y unzip curl wget git tree vim net-tools
```

Verify

```bash
tree --version
unzip -v
git --version
```

---

# Install Docker

```bash
curl -fsSL https://get.docker.com | sh
```

Enable Docker

```bash
systemctl enable docker
systemctl start docker
```

Verify

```bash
docker --version
docker ps
```

---

# Install Java and Maven

Install Java

```bash
apt install -y openjdk-21-jdk
```

Verify

```bash
java -version
javac -version
```

Install Maven

```bash
apt install -y maven
```

Verify

```bash
mvn -version
```

---

# Clone Repository

```bash
git clone https://github.com/sujalkamanna/employee-management-system-newrelic.git
```

```bash
cd employee-management-system-newrelic/crud-db-app
```

---

# Build Application

Build WAR File

```bash
mvn clean package
```

Expected Output

```text
BUILD SUCCESS
```

WAR Location

```text
target/employee-management-system-1.0.0.war
```

---

# Database Setup

## Create MySQL Container

```bash
docker run -d \
--name mysql \
-e MYSQL_ROOT_PASSWORD=root \
-p 3306:3306 \
mysql:5.7
```

Verify

```bash
docker ps
```

---

# Import Schema

```bash
docker exec -i mysql mysql -uroot -proot < database/01-schema.sql
```

---

# Import Sample Data

```bash
docker exec -i mysql mysql -uroot -proot < database/02-seed-data.sql
```

---

# Verify Database

```bash
docker exec -it mysql mysql -uroot -proot
```

```sql
USE employeedirectory;

SHOW TABLES;

SELECT * FROM tbl_employee;

SELECT * FROM tbl_login;
```

---

# Application Database Configuration

File:

```text
crud-db-app/src/com/sujalkamanna/util/DBConnectionUtil.java
```

Configuration:

```java
private static final String URL =
"jdbc:mysql://mysql:3306/employeedirectory";

private static final String USERNAME = "root";

private static final String PASSWORD = "root";
```

---

# Dockerfile

Location:

```text
crud-db-app/Dockerfile
```

```dockerfile
FROM tomcat:9.0-jdk8

COPY target/*.war /usr/local/tomcat/webapps/employee-directory.war

EXPOSE 8080
```

---

# Build Application Image

```bash
docker build -t employee-app:v1 .
```

Verify

```bash
docker images
```

---

# Create Docker Network

```bash
docker network create employee-network
```

---

# Attach MySQL Container

```bash
docker network connect employee-network mysql
```

---

# Run Application Container

```bash
docker run -d \
--name employee-app \
--network employee-network \
-p 8080:8080 \
employee-app:v1
```

Verify

```bash
docker ps
```

---

# Access Application

```text
http://EC2_PUBLIC_IP:8080/employee-directory
```

---

# Default Login Credentials

Example:

```text
Email:
admin@gmail.com

Password:
Admin@123
```

---

# New Relic Infrastructure Monitoring

## Install Agent

```bash
curl -Ls https://download.newrelic.com/install/newrelic-cli/scripts/install.sh | bash
```

Follow prompts.

---

# Verify Infrastructure Agent

```bash
systemctl status newrelic-infra
```

Expected

```text
active (running)
```

---

# New Relic Java APM Setup

Download Agent

```bash
wget NEW_RELIC_DOWNLOAD_URL
```

Extract

```bash
unzip newrelic-java.zip
```

Files:

```text
newrelic/
├── newrelic.jar
└── newrelic.yml
```

---

# Update Dockerfile

```dockerfile
FROM tomcat:9.0-jdk8

RUN mkdir -p /opt/newrelic

COPY target/*.war /usr/local/tomcat/webapps/employee-directory.war

COPY newrelic.jar /opt/newrelic/newrelic.jar
COPY newrelic.yml /opt/newrelic/newrelic.yml

ENV CATALINA_OPTS="-javaagent:/opt/newrelic/newrelic.jar"

EXPOSE 8080
```

---

# Rebuild Image

```bash
docker build -t employee-app:newrelic .
```

---

# Run New Relic Enabled Container

```bash
docker run -d \
--name employee-app \
--network employee-network \
-p 8080:8080 \
employee-app:newrelic
```

---

# Verify APM Connection

```bash
docker logs employee-app | grep -i relic
```

Expected

```text
Attaching the New Relic java agent
The agent has started and is connecting to New Relic
```

---

# Useful Docker Commands

View Containers

```bash
docker ps
```

View Images

```bash
docker images
```

Container Logs

```bash
docker logs employee-app
```

Access Application Container

```bash
docker exec -it employee-app bash
```

Access MySQL Container

```bash
docker exec -it mysql mysql -uroot -proot
```

Stop Containers

```bash
docker stop employee-app mysql
```

Start Containers

```bash
docker start employee-app mysql
```

Remove Containers

```bash
docker rm -f employee-app mysql
```

---

# Monitoring Features Demonstrated

* JVM Monitoring
* Throughput Monitoring
* Response Time Analysis
* Error Tracking
* Transaction Tracing
* Service Maps
* Infrastructure Metrics
* CPU Monitoring
* Memory Monitoring
* Disk Monitoring
* Container Monitoring

---

# Learning Outcomes

Through this project, the following concepts were implemented:

* Java Web Application Development
* JSP and Servlets
* MVC Architecture
* JDBC Integration
* MySQL Database Management
* Docker Containerization
* AWS EC2 Deployment
* Apache Tomcat Administration
* New Relic APM Monitoring
* New Relic Infrastructure Monitoring
* Application Observability
* Production-style Troubleshooting

---

# Acknowledgements

This project was originally inspired by and based on the JSP & Servlet CRUD Application tutorial created by Bushan Sirgur.

Original project and tutorial credits belong to Bushan Sirgur for the foundational Employee Directory application built using JSP, Servlets, JDBC, and MySQL.

The repository has been extensively modified and enhanced to include:

* Refactored package structure (`com.sujalkamanna`)
* Maven build improvements
* Docker containerization
* MySQL Docker deployment
* AWS EC2 hosting
* New Relic Java APM integration
* New Relic Infrastructure Monitoring
* Updated project structure and documentation
* Monitoring and observability implementation

Original Author:
Bushan Sirgur

Website:
https://bushansirgur.in

Original Tutorial:
https://bushansirgur.in/creating-mvc-database-web-application-in-jsp-and-servlets-create-read-update-delete/

Github Repo: https://github.com/scbushan05/jsp-servlet-database-crud-application

This repository is intended for educational, learning, and portfolio purposes.

---
# Author

Sujal Kamanna

DevOps & Cloud Engineer

GitHub: https://github.com/sujalkamanna