package com.bteam.fantasychess_server.service;

import com.bteam.fantasychess_server.data.entities.TokenEntity;
import com.bteam.fantasychess_server.data.repositories.TokenRepository;
import com.bteam.fantasychess_server.utils.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import security.CRC8;

import java.security.SecureRandom;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

/**
 * Used to generate and validate one time Tokens to authenticate WebSocket connections
 * during the Upgrade phase, to eliminate connections before they even happen.
 * This prevents invalid WebSocket connections clocking up the server's ports
 */
@Service
public class TokenService {

    private final static char[] CHARS_ARRAY = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_', '-'
    };
    private final static int TOKEN_LENGTH = 14;
    private final static long TOKEN_VALID_LENGTH_IN_SECONDS = 180;

    private final SecureRandom random = new SecureRandom();

    @Autowired
    TokenRepository tokenRepository;

    /**
     * Checks if a Token is Valid and removes it if true
     *
     * @param token 16 Character long token
     * @return weather Token is valid or not
     */
    public boolean invalidateToken(String token) {
        //Validate Checksum
        var checksum = getChecksum(token.substring(0, TOKEN_LENGTH));
        if (!token.substring(TOKEN_LENGTH).equals(checksum)) return false;

        //Validate if token exists
        var tokenEntity = tokenRepository.findById(token);
        if (tokenEntity.isEmpty()) return false;

        //Validate if not Expired
        var expireDate = LocalDateTime.ofEpochSecond(tokenEntity.get().getExpires(), 0, ZoneOffset.UTC);
        if (LocalDateTime.now(Clock.systemUTC()).isAfter(expireDate)) {
            tokenRepository.deleteById(token);
            return false;
        }

        //Token Valid
        tokenRepository.deleteById(token);
        return true;
    }

    /**
     * Generates a one time token, which is bound to a certain User's ID
     *
     * @param userId User ID in the DataBase
     * @return 16 Character long base64 String
     */
    public Pair<String, Long> generateToken(UUID userId) {
        // Ensure only one valid token exists per UserId
        System.out.println(tokenRepository.findAll());
        var exists = tokenRepository.findTokenEntitiesByUserId(userId);
        if (exists != null) invalidateToken(exists.getId());

        // Generate Token according to TOKEN_LENGTH
        var builder = new StringBuilder();
        for (int i = 0; i < TOKEN_LENGTH; i++) {
            builder.append(CHARS_ARRAY[random.nextInt(CHARS_ARRAY.length)]);
        }

        // Append Checksum
        builder.append(getChecksum(builder.toString()));

        //Generate Expiration Date
        var expiresEpochSeconds = LocalDateTime.now()
                .plusSeconds(TOKEN_VALID_LENGTH_IN_SECONDS)
                .toEpochSecond(ZoneOffset.UTC);

        // Save to Database
        var string = builder.toString();
        var entity = new TokenEntity(string, userId, expiresEpochSeconds);
        var item = tokenRepository.save(entity);

        return new Pair<>(item.getId(), expiresEpochSeconds);
    }

    /**
     * Get CRC8 Checksum for A Token
     *
     * @param token Token string
     * @return
     */

    private String getChecksum(String token) {
        var crc = new CRC8();
        crc.update(token.getBytes());
        return Long.toHexString(crc.getValue());
    }
}
