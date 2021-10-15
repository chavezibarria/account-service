# Account Service Application

### **How to build this project**
use 

_mvn package_ 

into the project directory to generate the jar file into the 'target' folder. _Note: you will need
to have installed maven to execute this commands._

### **Running the application**
use

_java -jar target/account-service-1.0.0-SNAPSHOT.jar_

to run the application.

Note: this application will run at localhost with port: 8001

### **API Endpoints**

        `http://localhost:8001/get-account/id`

will try to retrieve an account (if exists) with the specified Id. You will need to provide a Path param as the Id for the account.
Example of response body:
`{"id":"1","balance":100.00,"transfersToday":0,"errors":[]}`

        `http://localhost:8001/get-cad-exchange`
will try to retrieve an exchange rate for USD to CAD currency (a random value between 1.25 to 1.28). No Payload nor Param needed. 
Example of response body: `"1.2249599033215814"`

        `http://localhost:8001/execute-transfer`
will try to execute a Transaction to an Account which could be a Deposit or a Withdrawal,
depending on the value provided in the field deposit in the payload. Payload example:

`{
"id":"1",
"deposit":false,
"amount":100
}`

example of Response Body:

`{
"id": "1",
"balance": 99.99,
"transfersToday": 1,
"errors": []
}
`


_Note: All endpoints are secured with Basic Authentication approach, please use these credentials:_
    
    username: root
        pass: root
    





