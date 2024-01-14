# Discount Service


## Overview
Discount Service is an application that calculates discount for given order.\
It supports two types of discounts: percentage based and count based.\
If the product has discounts configured in the database of both types, all the most favorable values for a given type of discount are returned.


## How to use
1. Configure in db discounts -> test-data.sql in resources contains some example inserts
2. H2-console is available at: 
``http://localhost:8080/discount-service/h2-console`` \
Required credentials are in application.properties

3. POST /discount-service/calculate + OrderDto in body \
Example order for test-data.sql: \
``
{
"orderId" : 123,
"orderItems" : [
{
"productId" : "123",
"productPrice" :
{
"grossPrice" : 150
},
"quantityOfProductsOrdered" : 10
}
],
"price" : {
"grossPrice" : 1500
}
}
``


## Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- JUnit and Mockito

## Getting Started

### Clone the repository
To get started, clone this repository.

### Run the application
Once the build is successful, you can run the application using:

```bash
mvn spring-boot:run
```

### Documentation
The OpenApi will be available at `http://localhost:8080/discount-service/swagger-ui/index.html`.


## Running Tests
To run the test suite, execute:

```bash
mvn test
```

## Docker
`docker build -t your-image-name .` \
`docker run -p 8080:8080 your-image-name`

