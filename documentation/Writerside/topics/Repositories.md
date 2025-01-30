# Repositories

Repositories hold a link between Java Classes and the Database. They form a Programmatic representation
of the database and allow for easy definition of SQL queries using methods. Spring JPA is the framework that provides
these repository classes.

## Player Repository

The Player Repository links the [](Entities.md#player-entity) to a Table in the database. Along with this, it exposes
multiple methods to easily query for a specific player.