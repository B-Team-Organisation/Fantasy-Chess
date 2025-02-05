# Interceptors

`Author: Marc Matija`

## Text WebSocket Handler Extension
The `TextWebSocketHandlerExt` extends the base functionality of the [`TextWebSocketHandler`](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/socket/handler/TextWebSocketHandler.html)
by adding the Token invalidation and checking which player the websocket connection belongs to, it requests the
UUID associated with the token from the [](Services.md#token-service) and calls [](Services.md#register-client) on the 
WebSocket Service. It also forwards any TextMessages to the WebSocketService, as well as unregistering clients once they
disconnect.

## Client Identification Interceptor

When a client tries to connect to the `WebSocketServer`, it is not known initially known who he is and the IP address,
is not a reliable way to identify clients. This interceptor stands between a new connection and the
`TextWebSocketHandlerExt` to ensure, the client that tries to connect has previously authenticated with the server.
This Process is done using [Tokens](Authentication.md#token), which the interceptor relays to the 
[](Services.md#token-service) for validation. Should the token be valid, the interceptor lets the client connect,
otherwise it will stop the process and disconnect the client.