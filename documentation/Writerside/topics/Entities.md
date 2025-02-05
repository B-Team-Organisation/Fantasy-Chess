# Data Entities

`Author: Marc Matija`

Different to the entities inside the client, data entities are a construct from SpringBoot and map a Java Class
to a structure in a database. The specific type of database is not important in this case, as translations to different
databases happens using Spring JPA.

> The current implementation only makes use of entities for player specific data and all other data, such as Game State
> is stored in Memory. This should be expanded on in the future to allow for multiple instances of the game state server

## Player Entity

The Player Entity maps Player relevant information specific to players outside the running games. Like static data
that does not move once inside the game including, but not limited to:
- id: `UUID`
- name: `string`
- status: [Player Status](Models.md#player-model)
