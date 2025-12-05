### ECom Cart API

1. Start application

2. h2 DB console:- http://localhost:8081/h2-console (/resources/data.sql).
   DB Login using below details:-
    - Driver Class: org.h2.Driver
    - Jdbc URL:jdbc:h2:mem:mydb
    - User:sa (No Password)

   products table with data initialized - Id's:1001, 1002, 1003, 1004, 1005

### Endpoints Details:

1. Add product to cart :- http://localhost:8081/api/v1/products - POST
2. Update product quantity:- http://localhost:8081/api/v1/products/{productId} - PUT
3. Remove product from cart:- http://localhost:8081/api/v1/products/{productId} - DELETE

### Open API (Swagger) URL:

http://localhost:8081/swagger-ui/index.html
