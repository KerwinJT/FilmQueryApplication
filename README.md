## Film Application
This project focused on interacting with a database in a safe manner. Through PreparedStatements, we are able to search for and grab information while preserving the integrity of the database. This application further demonstrates that proper usage of database access can allow a user to search for films using fairly broad search arguments. From this, the application will produce a fully exhaustive list of search results. Currently, the application can search by FilmID and a Keyword.

### Topics and Technologies Used:
#### Topics
This application focused mainly on database access using SQL to query the database and display relevant information.

#### Technologies
--Eclipse IDE --Java 8 --Git --GitHub Repositories --MacOS Terminal --SQL --MAMP --Maven

### Lessons Learned
Creating the structure for querying the database went smooth in the beginning. After structuring the logic, I attempted to refactor the login process to reduce the amount of repetitive code in the DatabaseAccessorObject. This led to too many open connections while querying the database and caused the server to reject additional requests. Correcting this issue meant that I had to recode access to the database back into each of the querying methods. Once there, I purposely closed every connection within the try-catch block to further reduce the amount of open connections to the server. This bug was difficult to track because the logic inside the exception handling block was recoded to print a standard message that would not alarm the user. With this standard message, I failed to see that the server was overloaded with open requests and could not handle anymore. Had I kept the proper exception message in place, I would have been able to solve my bug more efficiently.
