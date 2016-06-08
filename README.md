This project implements a Java web app that allows the user to perform basic CRUD operations on a database table of address records.  The database is in SQL Server, and database access is done with standard JDBC methods.  The project also includes internationalization features, persistence of some settings via cookies, and encryption/decryption of the database connection information.

I have also been experimenting with implementing the same application with Spring MVC and Hibernate rather than basic servlets and JDBC. The code for this version is in progress, and is also uploaded here. 

The project can be deployed using the .war file included in the top level of the repo.  It was tested using Tomcat 8.0.
