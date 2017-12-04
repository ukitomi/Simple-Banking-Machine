# Simple Banking Machine
A program using Java, JDBC, SQL(DB2) that is built in Eclipse. Test cases are included in a text file. The user interface has not been develop yet, in order to navigate through the program, please use terminal.

## Getting Started
The readme will include step-by-step instructions on how to run and test the files.

### Prerequistes
* Eclipse IDE for [Java Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/oxygen1a)![](https://cdn.discordapp.com/attachments/316348168465809408/387137849796853761/unknown.png)
* Any functional Database (I used [IBM DB2](https://www.ibm.com/analytics/us/en/db2/trials/))![](https://cdn.discordapp.com/attachments/316348168465809408/387138474920378368/unknown.png)

### Installing
#### Step 1. 
Launch Eclipse. In Eclipse, click File -> New -> Java Project. Give the project a name, and leave the location as default. Click **finish** when done. 

#### Step 2.
Download the files needed under the repository, and store them under the project location.

#### Step 3.
Refresh the project in Eclipse. All the files should be visible.

#### Step 4.
Launch your database application. In your database application, create an actual database. [It looks like this in IBM DB2](https://cdn.discordapp.com/attachments/316348168465809408/387145801740189696/unknown.png)

#### Step 5.
Copy and paste the file contents under create.clp to your database app. [It looks like this in IBM DB2](https://cdn.discordapp.com/attachments/316348168465809408/387355756099403797/unknown.png)

Those are the create table statements needed in order to test our program.

#### Step 6.
At this point, all the files are ready to go for testing. Tables are included in the database too.


## Running the tests
=> Make sure that the java files are **runnable**, and the database contain the **tables needed**.

Open terminal and go to the directory where files are located.
Copy and paste the following lines into the terminal.
```
javac BankingSystem.java
javac ProgramLaunhcer.java
java -cp ":../lib/db2jcc4.jar" ProgramLauncher  db.properties
```

and the output should look exactly the same like in output.txt file.

To begin testing, simply follow the instructions on the terminal.

## Built with
* ![Java](https://www.oracle.com/java/index.html) - The program framework
* ![JDBC](http://www.oracle.com/technetwork/java/javase/jdbc/index.html) - Connect Java with SQL
* SQL - Managed data in the database

## Author
* ![Yuki Ou](https://github.com/ukitomi)


