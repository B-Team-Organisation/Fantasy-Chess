# Controller

Controller are a Spring Boot component that manages incoming HTTP requests. In this project they are used for any
communication before a WebSocket connection has been established.

## Health

The Health controller exposes a single HTTP endpoint, which is used to check, weather or not the service is currently
active and running.

### `api/v1/health`
Method: `GET`

Response:   
Code: `200`   
Type: `String`  
Body: `Ok`

## Authentication

The authentication controller exposes 2 HTTP endpoints, which act as a register endpoint and a token endpoint. It is
expected for the client to first register itself under a username and get a UUID, then generate a token with that
UUID so it can be reidentified once he has connected using WebSockets. This is done, so the registration process can
later be swapped out for an OICD or 0auth authentiction process to be able to easily recognize returning players.

> In the current implementation the user merely gets an ID for his username more information can be found in the java
> docs.
> {style="warning"}

> More Information on the authentication flow can be found [here](Authentication.md#auth-flow)


### `api/v1/register`  
Method: `POST`

**Request:**
Type: `application/json`  
Body:
```json
{
  "username": "<USERNAME>"
}
```


**Response:**   
Code: `200`  
Type: `UUID`  
Body: `{UUID}`  


### `api/v1/token`

<warning>This endpoint is currently not secured, this goes against the authentication flow, as this is THE endpoint
that needs to be secured, however, to add proper authentication and platform independent authentication, you just need
to secure this endpoint.</warning>

Method: `GET`

**Request:**  
Type: `application/json`  
Header:

<deflist type="narrow">
<def title="X-USER-ID">`string`</def>
</deflist>

**Response:**   
Code: `200`  
Type: `UUID`  
Body: `{UUID}`