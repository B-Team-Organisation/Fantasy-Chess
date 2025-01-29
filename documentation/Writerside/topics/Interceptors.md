# Interceptors

## Client Identification Interceptor

When a client tries to connect to the `WebSocketServer`, it is not known initially known who he is and the IP address,
is not a reliable way to identify clients. This interceptor stands between a new connection and the
`TextWebSocketHandlerExt` to ensure, the client that tries to connect has previously authenticated with the server.
This Process is done using [Tokens](Authentication.md#token), which the interceptor relays to the 
[](Services.md#token-service) for validation. Should the token be valid, the interceptor lets the client connect,
otherwise it will stop the process and disconnect the client.