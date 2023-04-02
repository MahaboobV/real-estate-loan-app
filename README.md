# Real Estate Loan Application 

#Loan Application

Spring boot Rest API application which contains different API end points to register loan application and to retrieve the customer loan application as well.
The rest end points are secured by OAuth2. Authorization based on the user role such as Customer or Adviser.

Customer Role based user can have access to end point which is used to store the Loan Application details.

Adviser Role based used can have access to end points which is used to get all the Loan Applications and to get the each loan application details

#Aws services integrated

AWS cognito as IDP ( Authentication server)
AWS Lamda as computing service
AWS DynamoDB as backend database.
AWS APIGateway

Workflow
==========
Each request from client (browser or UI based applcation) will be handled API gateway with Authorizer. Once the request is authorized with valid access token request will be forward to respective Lambda function which will compute the service and respond back with required response. The response will be channelled back to client through API gateway.
If the request does not have a valid token , request will be declined by API gateway.
Validity of the request authorization token will be processed at Rest end point business logic and decided to serve the request or reject.

#Deployment

Application is designed to be deployed as Serverless application in AWS cloud 




