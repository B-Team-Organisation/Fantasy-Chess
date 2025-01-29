# Services

All services are SpringBoot services classes, which are Singletons within the Server context and can automatically
be referenced in fields using dependency Injection. They are the backbone of the server and form the link between
incoming requests and the database.

## Token Service

The token service is used to generate a token for a given user and check its validity.

<procedure title="Token Generation" id="token-generation">
<step>
Generate a random 14 character <a href="https://base64.guru/standards/base64url">Base64URL</a> string
</step>
<step>
Generate the CRC8 checksum for the string and append it to the token.
</step>
<step>
Generate the expiry date of the token.
</step>
<step>
Assign the token to a player's UUID and add it, along with it's <code>expiration</code> into the database. 
</step>
<p>
In the end, the Token should be written to the database and have a structure, similar to this:<br /> 
<code>V3D5qNVl8IpP2y_qEgQ24</code>
</p>
</procedure>
<procedure title="Token Validation" id="token-validation">
    <step>
        Check token for it's CRC8 checksum. Should the CRC8 checksum of the first 14 characters not match with the
        provided checksum, then discard the token and stop the connection attempt.
    </step>
    <step>
        Should the checksum match, then it is safe to assume the token exits in the database, thus fetch the 
        <code>expiration</code> and the <code>playerId</code> from the <code>Token repository</code>
    </step>
    <step>
        Check if the token is expired and if so, stop the current connection attempt and delete the token from the.
    </step>
<step>
If all steps succeed, you can safely say, that the token is valid.
</step>
<warning>
Tokens are supposed to be used only once, so once a Token was found to be valid, it is to be invalidated right
after the required operations with the token have succeeded.
</warning>
</procedure>
<procedure title="Token Invalidation" id="token-invalidation">
    <p>To invalidate a token, simply delete it from the <code>TokenRepository</code></p>
</procedure>

## WebSocket Service

The Websocket Service is used for handling all active WebSocket connections and routing the incoming [Packets](Packet.md)
to the correct [](Packet-Handler.md).