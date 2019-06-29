package com.philipbui.sharepoint.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipbui.sharepoint.models.AccessTokenResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("WeakerAccess")
public class AccessTokenResponseTest {

    @Test
    public void testJackson() throws IOException {
        AccessTokenResponse accessTokenResponse = new ObjectMapper().readValue(
                getClass().getClassLoader().getResourceAsStream("access-token.json"),
                AccessTokenResponse.class
        );
        assertEquals("abcd1234", accessTokenResponse.getAccessToken());
    }
}
