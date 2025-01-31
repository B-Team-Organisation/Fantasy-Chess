# Networking

Clientside networking is handled by the [`gdx-websockets`](https://github.com/MrStahlfelge/gdx-websockets) library

## WebSocket Service

Forms the backbone of the clientside networking, managing packet routing and 

## WebSocket Client

An implementation of the `WebSocketListener` from gdx-websocket, which maps method calls to [Event](Events.md)
invocations. Notable events that get mapped include:
- `onOpenEvent`
- `onCloseEvent`
- `onErrorEvent`
- `onTextEvent`

This is done for easier access to the exposed methods within the [](#websocket-service).

## HTTP Response Callback Listener

Maps exposed by the HttpResponseListener to consumers to be able to dynamically assign them at runtime.

## Auth

Handled by sending multiple HTTP requests through the [](#http-response-callback-listener)

<procedure>
<step>
    The user calls <code>registerAndConnect</code> passing in the player's username and starting the 
    connection chain.
</step>
<step>
    <code>registerClient</code> gets called, sending a POST request to 
    <a href="Controller.md#api-v1-register"><code>https://{BASEURL}/api/v1/register</code></a> with the players
    username in the body of the request.
</step>
<step>
    After obtaining the player's id it gets written to the LibGDX player preferences for later use and passed into
    the <code>getToken</code> method.
</step>
<step>
    A GET request is sent to <a href="Controller.md#api-v1-register"><code>https://{BASEURL}/api/v1/register</code></a>
    with the uuid in the <code>X-USER-ID</code> header.
</step>
<step>
    upon receiving the token, it is written to a variable for later use.
</step>
<p>
The client is now authenticated and can connect to the websocket server.
</p>
</procedure>

> For more information on the Auth Flow, click [here](Authentication.md#auth-flow)

## Packet Handler


## Deserialization

