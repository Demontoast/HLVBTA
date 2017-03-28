# README #


### What is this repository for? ###

* Starter template for Spark Java based RESTful api.

### How do I get set up? ###

1. You will need to host your mysql database and populate it with the world data available. Instructions at 
https://dev.mysql.com/doc/world-setup/en/world-setup-installation.html 
3. To run the web client:

* Build the jar artifact (Build -> Build artifacts)
* Locate seniorproject.jar under the out folder
* Create a DB_INFO file with database connection information (see class ApiImplementation) in the same folder as the jar file
* Run the jar file:
		java -jar seniorproject.jar  
* In your browser, open http://localhost:8080/index.html 

![Image of web page](images/citiespage.png)
