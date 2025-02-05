# Common

`Author: Marc Matija`

The common module contains all logic, that needs or should be shared between client and server. In the deployment a
compiled version of this module lives inside the client and the server. Things that get handled here contain various
[](Models.md), the [](Packet.md) and [](Turn-Logic.md) on top of various helper classes. Just like the Client, common
is restricted to Java 11 and all GWT compatibility, even if the server is able to handle a higher version number.