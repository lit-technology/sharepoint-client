package com.philipbui.sharepoint;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipbui.sharepoint.models.GetListResponse;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("WeakerAccess")
public class SharepointClientTest {

    private CloseableHttpClient httpClientMock;
    private SharepointClient client;

    public static class ExampleListItem {

        private String test;

        @JsonCreator
        public ExampleListItem(@JsonProperty("test") String test) {
            this.test = test;
        }
    }

    @BeforeEach
    void setUp() {
        httpClientMock = Mockito.mock(CloseableHttpClient.class);
        client = new SharepointClient(httpClientMock, new ObjectMapper());
    }

    @Test
    public void testGenerics() throws IOException {
        GetListResponse<ExampleListItem> getListResponse = client.getReader().readValue(getClass().getClassLoader().getResourceAsStream("get-list-response.json"), new TypeReference<GetListResponse<ExampleListItem>>() {
        });
        assertEquals("abcd1234", getListResponse.getValue().get(0).test);
    }

    private CloseableHttpResponse mockResponse(String resource) throws IOException {
        HttpEntity httpEntity = Mockito.mock(HttpEntity.class);
        when(httpEntity.getContent()).thenReturn(getClass().getClassLoader().getResourceAsStream(resource));

        StatusLine statusLine = Mockito.mock(StatusLine.class);
        when(statusLine.getStatusCode()).thenReturn(200);

        CloseableHttpResponse response = Mockito.mock(CloseableHttpResponse.class);
        when(response.getEntity()).thenReturn(httpEntity);
        when(response.getStatusLine()).thenReturn(statusLine);
        return response;
    }

    @Test
    public void testGetAccessToken() throws IOException {
        CloseableHttpResponse response = mockResponse("access-token.json");
        when(httpClientMock.execute(any())).thenReturn(response);

        String accessToken = client.getAccessToken("", "", "", "", "");
        assertEquals("abcd1234", accessToken);
    }

    @Test
    public void testGetListItemsComplete() throws IOException {
        CloseableHttpResponse response = mockResponse("get-list-response.json");
        when(httpClientMock.execute(any())).thenReturn(response);

        List<ExampleListItem> listItems = client.getListItemsComplete("", "", "", "", ExampleListItem.class);
        assertNotNull(listItems);
        assertFalse(listItems.isEmpty());
        assertEquals("abcd1234", listItems.get(0).test);
    }
}
