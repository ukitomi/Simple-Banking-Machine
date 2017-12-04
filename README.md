# Simple Banking Machine
A program using Java, JDBC, SQL(DB2) built in Eclipse. 

## Getting Started
The readme will include step-by-step instructions on how to run and test the files.

### Prerequistes
* Eclipse IDE for [Java Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/oxygen1a)![](https://cdn.discordapp.com/attachments/316348168465809408/387137849796853761/unknown.png)
* Any functional Database (I used [IBM DB2](https://www.ibm.com/analytics/us/en/db2/trials/))![](https://cdn.discordapp.com/attachments/316348168465809408/387138474920378368/unknown.png)

### Installing
#### Step 1. 
Launch Eclipse. In Eclipse, click File -> New -> Java Project. Give the project a name, and leave the location as default unless you want to specify the location and store the test files somewhere. 

#### Step 2.
Click **finish** when done. 

#### Step 3.
Download the files needed under the repository, and store them under the eclipse directory.

#### Step 4.
Launch your database application. In your database application, create an actual database. ![It looks like this in IBM DB2](https://cdn.discordapp.com/attachments/316348168465809408/387145801740189696/unknown.png)

#### Step 5.
Copy and paste the file contents under create.clp. Those are the create table statements needed in order to test our program.


## Running the tests
=> Make sure that the java files are **runnable**, and the database contain the **tables needed**.

Open terminal and go to the directory where files are located.
Copy and paste the following lines into the terminal.
```
javac BankingSystem.java
javac ProgramLaunhcer.java
java -cp ":../lib/db2jcc4.jar" ProgramLauncher  db.properties
```

and your output should look like this:

:: TEST - CONNECTING TO DATABASE
:: TEST - SUCCESSFULLY CONNECTED TO DATABASE
:: TEST - DISCONNECTED FROM DATABASE

:: CREATE NEW CUSTOMER - RUNNING
:: CREATE NEW CUSTOMER - SUCCESS


:: CREATE NEW CUSTOMER - RUNNING
:: CREATE NEW CUSTOMER - SUCCESS


:: CREATE NEW CUSTOMER - RUNNING
:: CREATE NEW CUSTOMER - SUCCESS


:: OPEN ACCOUNT - RUNNING
:: OPEN ACCOUNT - SUCCESS


:: OPEN ACCOUNT - RUNNING
:: OPEN ACCOUNT - SUCCESS


:: OPEN ACCOUNT - RUNNING
:: OPEN ACCOUNT - SUCCESS


:: OPEN ACCOUNT - RUNNING
:: OPEN ACCOUNT - SUCCESS


:: ACCOUNT SUMMARY - RUNNING
number      balance    
----------- -----------
       1000          10
       1001         100
-----------------------
total               110
:: ACCOUNT SUMMARY - SUCCESS


:: REPORT A - RUNNING
id          name            gender age         total      
----------- --------------- ------ ----------- -----------
102         Misty           F      18          500        
100         Ash             M      10          110        
101         Pikachu         M      25          25         
:: REPORT A - SUCCESS


:: DEPOSIT - RUNNING
:: DEPOSIT - SUCCESS


:: WITHDRAW - RUNNING
:: WITHDRAW - SUCCESS


:: TRANSFER - RUNNING
:: TRANSFER - SUCCESS


:: CLOSE ACCOUNT - RUNNING
:: CLOSE ACCOUNT - SUCCESS


:: ACCOUNT SUMMARY - RUNNING
number      balance    
----------- -----------
       1000         310
-----------------------
total               310
:: ACCOUNT SUMMARY - SUCCESS


:: REPORT A - RUNNING
id          name            gender age         total      
----------- --------------- ------ ----------- -----------
102         Misty           F      18          500        
100         Ash             M      10          310        
101         Pikachu         M      25          25         
:: REPORT A - SUCCESS


:: REPORT B - RUNNING
average    
-----------
262        
:: REPORT B - SUCCESS

  --------------------------------------
  --------------------------------------
|| Welcome to the Self Service Banking System! - Main Menu
  --------------------------------------
  --------------------------------------
1. New Customer
2. Customer Login
3. Exit
Note: NUMBER ONLY!


