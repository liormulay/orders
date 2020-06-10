#Orders system
In this project customer can make an order see his orders ect.
The admin can see all orders add to the stock ect.

There are four entities in the database (take a look at EER diagram.png file):
* **User** - the user that login to the system can be customer or manager.
* **Product** - entity for the products in the stock
* **Item** - item has bought.  
* **Order** - order that user made contains items.

To make it work create those entities in mysql and connect to it with application.properties file. <br>
The authentication is by a token after user got it in registration he needs to send it as Authorization header.