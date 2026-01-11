# Database Systems Project

A comprehensive educational project demonstrating various database technologies and distributed storage systems. This project includes implementations for MySQL, Hibernate ORM, MongoDB, Redis, and Memcached.

## Table of Contents

- [Overview](#overview)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup Instructions](#setup-instructions)
- [Database Schemas](#database-schemas)
- [Usage](#usage)
- [Building the Project](#building-the-project)
- [Running Tests](#running-tests)
- [Components](#components)

## Overview

This project is designed as a learning platform for understanding heterogeneous storage systems on the cloud. It demonstrates:

- **Relational Database Operations** using MySQL with JDBC
- **Object-Relational Mapping (ORM)** with Hibernate
- **NoSQL Database** operations with MongoDB
- **In-Memory Caching** with Redis and Memcached
- **Distributed Locking** mechanisms using Redis
- **Data Analysis** with Python and pandas

## Technologies

- **Java 8** - Primary programming language
- **Maven** - Build and dependency management
- **MySQL 5.7+** - Relational database
- **Hibernate 5.2** - ORM framework
- **MongoDB** - NoSQL document database
- **Redis** - In-memory data structure store
- **Memcached** - Distributed memory caching system
- **Python** - Data analysis scripts
- **SQLite** - Embedded database for Yelp dataset
- **JUnit** - Testing framework
- **Lombok** - Java annotation library for reducing boilerplate code

## Prerequisites

Before running this project, ensure you have the following installed:

- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.x
- MySQL Server 5.7 or higher
- MongoDB (if running MongoDB tasks)
- Redis (if running Redis tasks)
- Python 3.x with pandas and MySQLdb libraries (for data analysis tasks)
- Git

## Project Structure

```
.
├── src/
│   ├── main/
│   │   ├── java/edu/cmu/cs/cloud/
│   │   │   ├── MySQLTasks.java          # MySQL database operations
│   │   │   ├── YelpApp.java             # Hibernate ORM implementation
│   │   │   ├── MongoDBTasks.java        # MongoDB operations
│   │   │   ├── Redis.java               # Redis cache implementation
│   │   │   ├── RedisLock.java           # Distributed locking with Redis
│   │   │   ├── Memcached.java           # Memcached implementation
│   │   │   ├── Business.java            # Business entity model
│   │   │   ├── Config.java              # Configuration management
│   │   │   ├── ReservationApp.java      # Reservation system
│   │   │   └── Server.java              # Server utilities
│   │   └── resources/
│   │       ├── hibernate.properties     # Hibernate configuration
│   │       └── configuration.yaml       # Application configuration
│   └── test/
│       └── java/edu/cmu/cs/cloud/       # Test cases
├── create_yelp_database.sql             # Yelp database schema and data loading
├── hibernate_sample_db.sql              # Sample database for Hibernate
├── q1.py                                # Python data analysis script
├── yelp_db.sqlite3                      # SQLite database with Yelp data
├── runner.sh                            # Shell script runner
├── pom.xml                              # Maven project configuration
└── README.md                            # This file
```

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd mysql-db
```

### 2. Configure MySQL Database

Set the MySQL password as an environment variable:

```bash
export MYSQL_PWD='your_mysql_password'
```

### 3. Create the Yelp Database

Load the Yelp dataset into MySQL:

```bash
mysql --username=<username> -h <hostname> --port <port> --password=$MYSQL_PWD --local-infile=1 < create_yelp_database.sql
```

Make sure you have the following TSV files in the same directory:
- `yelp_academic_dataset_businesses.tsv`
- `yelp_academic_dataset_checkin.tsv`
- `yelp_academic_dataset_user.tsv`
- `yelp_academic_dataset_tip.tsv`
- `yelp_academic_dataset_review.tsv`

### 4. Create Sample Database for Hibernate

```bash
mysql --username=<username> -h <hostname> --port <port> --password=$MYSQL_PWD --local-infile=1 < hibernate_sample_db.sql
```

### 5. Configure MongoDB (Optional)

If running MongoDB tasks, set the connection string:

```bash
export CS='mongodb://username:password@host:port/database'
```

### 6. Install Python Dependencies (Optional)

For data analysis tasks:

```bash
pip install pandas mysqlclient
```

## Database Schemas

### Yelp Database (`yelp_db`)

The project uses a Yelp academic dataset with the following tables:

#### 1. **businesses**
Stores business information including name, location, ratings, and categories.

#### 2. **users**
Contains Yelp user profiles with review statistics and social connections.

#### 3. **reviews**
Review data linking users and businesses with ratings and text content.

#### 4. **tips**
Short tips from users about businesses.

#### 5. **checkins**
Business check-in data over time.

### Sample Database (`sample_db`)

A simple database for Hibernate examples:

#### 1. **course**
Course information with course ID and name.

#### 2. **project**
Project details linked to courses via foreign key.

## Usage

### Building the Project

Compile and package the project:

```bash
mvn clean package
```

This creates `target/database_tasks.jar` with all dependencies included.

### Running MySQL Tasks

Execute various MySQL queries:

```bash
# Demo example
java -cp target/database_tasks.jar edu.cmu.cs.cloud.MySQLTasks demo

# Run specific queries (q2 through q15)
java -cp target/database_tasks.jar edu.cmu.cs.cloud.MySQLTasks q2
java -cp target/database_tasks.jar edu.cmu.cs.cloud.MySQLTasks q3
# ... and so on
```

### Running Hibernate Application

```bash
java -cp target/database_tasks.jar edu.cmu.cs.cloud.YelpApp
```

### Running MongoDB Tasks

```bash
java -cp target/database_tasks.jar edu.cmu.cs.cloud.MongoDBTasks <task_name>
```

### Running Python Data Analysis

Execute the pandas-based analysis:

```bash
python q1.py
```

This script reads data from MySQL and generates descriptive statistics for the `review_count` and `stars` columns in the businesses table.

### Using the Shell Runner

The `runner.sh` script provides a convenient interface for running various tasks:

```bash
./runner.sh setup    # Initial setup
./runner.sh <task>   # Run specific task
```

## Running Tests

Execute the test suite:

```bash
mvn test
```

Run tests with coverage report:

```bash
mvn clean test jacoco:report
```

The coverage report will be available at `target/site/jacoco/index.html`.

## Components

### 1. MySQL Tasks (`MySQLTasks.java`)

Demonstrates JDBC connectivity and SQL query execution:
- Database connection management
- Prepared statements
- Result set processing
- Transaction handling
- Complex queries on Yelp dataset

**Key Features:**
- Questions (q2-q15) covering various SQL operations
- Aggregate functions and joins
- Data filtering and sorting
- Performance optimization techniques

### 2. Hibernate ORM (`YelpApp.java`)

Object-Relational Mapping implementation:
- Entity mapping with annotations
- Session management
- HQL (Hibernate Query Language)
- Relationship mapping (One-to-Many, Many-to-One)
- Lazy loading and eager fetching

**Configuration:**
- Database connection settings in `hibernate.properties`
- Entity classes with Lombok annotations
- SessionFactory configuration

### 3. MongoDB Tasks (`MongoDBTasks.java`)

NoSQL database operations:
- Document insertion and retrieval
- Query filters and projections
- Aggregation pipelines
- Index management
- Connection pooling

### 4. Redis Implementation (`Redis.java`, `RedisLock.java`)

In-memory caching and distributed locking:
- Key-value storage
- Data expiration (TTL)
- Distributed lock implementation
- Pub/Sub messaging
- Connection management with Jedis

**RedisLock Features:**
- Acquire and release locks
- Lock timeout handling
- Thread-safe operations
- Deadlock prevention

### 5. Memcached (`Memcached.java`)

Distributed memory caching:
- Cache get/set operations
- Cache invalidation
- Connection pooling
- Serialization of Java objects

### 6. Data Analysis (`q1.py`)

Python-based data analysis:
- SQL query execution with pandas
- DataFrame operations
- Descriptive statistics generation
- CSV output formatting

**Output Format:**
```
count,174567.00,174567.00
mean,30.14,3.63
std,98.21,1.00
min,3.00,1.00
25%,4.00,3.00
50%,8.00,3.50
75%,23.00,4.50
max,7361.00,5.00
```

## Development Guidelines

### Code Style

- Follow Java naming conventions
- Use Lombok annotations to reduce boilerplate
- Implement proper exception handling
- Close database connections in finally blocks or use try-with-resources
- Add meaningful comments for complex logic

### Adding New Features

1. Create feature in appropriate Java class
2. Add corresponding tests in `src/test/java`
3. Update this README if adding new functionality
4. Run tests to ensure no regressions

### Database Best Practices

- Use prepared statements to prevent SQL injection
- Implement connection pooling for production
- Close ResultSet, Statement, and Connection objects
- Use transactions for multi-step operations
- Index frequently queried columns

## Common Issues and Troubleshooting

### MySQL Connection Issues

**Problem:** `Unable to connect to MySQL database`

**Solutions:**
- Verify MySQL server is running
- Check `MYSQL_PWD` environment variable is set
- Confirm username and hostname are correct
- Ensure MySQL is configured to allow local connections
- Check if the database `yelp_db` exists

### Build Failures

**Problem:** Maven build fails

**Solutions:**
- Ensure Java 8 or higher is installed
- Run `mvn clean` before building
- Check internet connection for dependency downloads
- Verify `pom.xml` is not corrupted

### Missing Data Files

**Problem:** Data loading fails with file not found

**Solutions:**
- Ensure TSV files are in the same directory as SQL script
- Check file permissions
- Use absolute paths if necessary
- Enable `local-infile` in MySQL configuration

### Hibernate Configuration Issues

**Problem:** Hibernate session factory fails to initialize

**Solutions:**
- Verify `hibernate.properties` exists in resources
- Check database connection parameters
- Ensure entity classes are properly annotated
- Validate dialect configuration matches your database version

## License

This project is for educational purposes as part of a Cloud Computing course at Carnegie Mellon University.

**Copyright 2021-2022: Cloud Computing Course, Carnegie Mellon University**

Unauthorized distribution of copyrighted material, including unauthorized peer-to-peer file sharing, may subject the students to the fullest extent of Carnegie Mellon University copyright policies.

## Acknowledgments

- Carnegie Mellon University Cloud Computing Course
- Yelp Dataset (for academic use)
- Apache Software Foundation (Hadoop, Maven)
- Hibernate ORM project
- MongoDB Inc.
- Redis Labs

## Contact and Support

For questions or issues related to this project, please refer to the course materials or contact the course instructors.

---

**Note:** This is an educational project. Always follow your institution's academic integrity policies when working on coursework.
