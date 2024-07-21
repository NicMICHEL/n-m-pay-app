# PayBuddy

A web application for easily managing financial transactions between users.
Registration can be done directly or from one's Google account.
Each user is identified by his email which cannot be modified.
As this is a prototype, the accounts are artificially provisioned from MySql WorkBench.
Each transaction is charged 0.5% of the transaction amount.

## Class diagram

The class diagram and database structure are shown below (ClassDiagram.png file under the resources folder) :

<img src="src\main\resources\ClassDiagram.png" width="400"/>


## Prerequisites

This app uses Java to run and stores the data in Mysql DB.
To install the software, you need 
Java 17 ,
Maven 3.9.6 ,
Mysql 8.0.34 

After installing mysql, you will be asked to configure the password for the default root account.
This code uses the default root account to connect and the password can be set as rootroot.
If you add another user/credentials make sure to change the same in the code base.

## Running App

Post installation of MySQL, Java and Maven, you will have to set up the tables and data in the data base.
For this, please run the sql commands present in the PayBuddy.sql file under the resources folder in the code base.
Four users are already registered in the database : ben@gmail.com (password : ben), lola@gmail.com (password : lola),
greg@gmail.com (password : greg), aria@gmail.com (password : aria).

## OAuth2

To implement OAuth2 authentication, you must enter the client-id and client-secret variables
in the application-properties file.

## Testing

The app has unit tests written. To run the tests from maven, execute the following command : mvn test




