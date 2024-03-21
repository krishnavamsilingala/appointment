Install Maven build tool

Configure Database credentials(username,password) in appication.yml file in resources directory

Run following steps
  //to Build the application
  1. mvn clean install
  //to Start the application
  2. mvn spring-boot:run


Swagger Endpoint: http://localhost:8080/agency/swagger-ui/index.html

1.  Preloaded necessary data through Main Class

2.  The strategy I used is by generating slots(materializing conflicts) beforehand for high Concurrency and 
    high Performance(reduced latency).We can generate slots as per smallest unit possible(5 mins,10mins) , so that we can avoid overlap bookings if there are
    different duration types availability of operator.

3.  Concurrency is controlled by unique constraints and Optimistic Locking on Slot entity

4.  We can use cron/scheduler jobs to generate slots for operators based on their config(duration size,availability etc,.)

