# Patient Tracker Service
A Spring Boot REST service for communication between the PatientTrackerApp front end and a MongoDB Atlas Cluster containing a simple database with patient records.

## Getting Started
These instructions will get the project up and running on your local machine and working with all intended applications.

### Prerequisites
In order for this project to run as intended there are two prerequisites that are needed to communicate with it. These are:
* [PatientTrackerApp](https://github.com/AlecSmith96/PatientTrackerApp) -
A front-end application that needs to be cloned from its repository, it provides the user interface with the service.

* External Database to contain and retrieve data on patients, originally using a [MongoDB Atlas Cluster](https://www.mongodb.com/cloud/atlas).
* * MongoDB Atlas is a cloud service containing the database that the service inserts and retrieves data from.
* * The database does not need to be a MongoDB Atlas Cluster and can be any external database that can be connected to via a Java back end. Any new database used can implement the 
```
DatabaseConnector
```
interface to ensure correct functionality for communicating with the controller methods.
The database tables should look like:

Patients
| id  | name       | email          | phoneNumber | dateOfBirth | triageDate          |
| --- | ---------- | -------------- | ----------- | ----------- | ------------------- |
| P-1 | John Smith | john@email.com | 01234567890 | 1996-12-23  | 22/05/2020 03:15:41 |

Allergies
| id  | description | patient_id |
| --- | ----------- | ---------- |
| A-1 | Paracetamol | P-1        |

## Setting up environment
In order for the service to communicate with any external database it will require a database uri, username, password and database name. These can be added to the 
```
application.properties
```
file and accessed in any new DatabaseConnector implementation.

## Built With
* [Spring Boot Web](https://spring.io/projects/spring-boot) - The web-framework used
* [Maven](https://maven.apache.org/) - Dependency Management
