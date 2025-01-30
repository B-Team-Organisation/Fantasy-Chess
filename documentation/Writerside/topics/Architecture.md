# Architecture

The Project has been layed out to feature 3 Main components.

- [Commons](Common.md)
- [Client](Client.md)
- [Server](Server.md)

The [Commons](Common.md) project is a plain Java Project without dependencies, which handles logic that is shared 
between client and server. This includes, but is not limited to the [Turn Logic Service](Turn-Logic-Service.md),
[JsonDTOs](JsonDTOs.md) and [Models](Models.md).

The [Client](Client.md) is a LibGDX Project that is exported to JavaScript and hosted in the browser, where LibGDX 
is mainly used for it's rendering features, as well as for tile maps and input handling.

The [Server](Server.md) is a SpringBoot Projects that acts as a WebSocket Server to transmit the current status of the 
board, execute the Turn Logic and relay the turns that players have made.

## Network Structure

The client and server are constructed so that they may run independent of one another. The client should be served to
the browser using an external static file service f.e. `npx serve`, while the server should run on it's own.
The goal is to be able to scale this infinitely with the client and server being stateless connected to an external
distributed database system.

![](../img/architecture/architecture.drawio.svg)

> The current implementation still has the server being stateful and not make full use of the database due to time
> constraints, this could be expanded upon in the future. See [](Services.md).