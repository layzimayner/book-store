This project was created as a useful tool to simplify accounting, sales,
and information gathering for use by a bookstore.

The project is built on Spring, with the addition of Boot, Security, and Data JPA,
aimed at reducing boilerplate code and simplifying testing.
To ensure the security of all sensitive data and maintain the stateless nature of the application,
I implemented JWT tokens. Additionally, the inclusion of Swagger provides the ability to 
interactively test how the application works.

In addition to standard controller endpoints like delete, create, update, find by ID, and get all,
the project also includes publicly accessible endpoints such as:

* POST auth/login and POST auth/register

More specific endpoints include:

* GET category/{categoryId}/books
 Returns a list of books that belong to the category specified by categoryId.

* POST orders
 Requires an authenticated user to create an order based on their account and shopping cart.

Before launching the application, remember to configure the application. properties file
and .env. An example of the variables is provided in .env.sample.
Additionally, the MySQL database must include a schema named book_store.
![Alt text](screenshot/Знімок екрана 2024-12-28 222347.png "Screenshot sample")
![Alt text](screenshot/Знімок екрана 2024-12-28 222150.png "Screenshot sample")

As my first project, it raised many questions and challenges, especially during the testing phase.
However, thanks to advice from kind and supportive individuals and my eagerness to learn,
I am proud to present this work.






