package com.philipbui.sharepoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.philipbui.sharepoint.models.AccessTokenResponse;
import com.philipbui.sharepoint.models.GetListResponse;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class SharepointClient {

    private static final Logger logger = LogManager.getLogger(SharepointClient.class);
    private final CloseableHttpClient client;
    private final ObjectMapper reader;

    public SharepointClient() {
        this(HttpClientBuilder.create().build(), new ObjectMapper());
    }

    SharepointClient(CloseableHttpClient client, ObjectMapper reader) {
        this.client = client;
        this.reader = reader;
    }

    public String getAccessToken(String clientID, String clientSecret, String realm, String principal, String targetHost) throws IOException {
        String uri = String.format("https://accounts.accesscontrol.windows.net/%s/tokens/OAuth/2", realm);
        HttpPost request = new HttpPost(uri);
        request.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        List<NameValuePair> urlEncodedForm = new ArrayList<>(4);
        urlEncodedForm.add(new BasicNameValuePair("grant_type", "client_credentials"));
        urlEncodedForm.add(new BasicNameValuePair("client_id", String.format("%s@%s", clientID, realm)));
        urlEncodedForm.add(new BasicNameValuePair("client_secret", clientSecret));
        urlEncodedForm.add(new BasicNameValuePair("resource", String.format("%s/%s@%s", principal, targetHost, realm)));
        request.setEntity(new UrlEncodedFormEntity(urlEncodedForm, "UTF-8"));
        try (CloseableHttpResponse response = getClient().execute(request)) {
            logger.info("GET /" + uri);
            return getReader().readValue(response.getEntity().getContent(), AccessTokenResponse.class).getAccessToken();
        } catch (Exception e) {
            throw new IOException("Error getting Access Token GET /" + uri, e);
        }
    }

    public <T> List<T> getListItemsComplete(String host, String site, String list, String accessToken, Class<T> clazz) throws IOException {
        GetListResponse<T> listResponse = getListItemsJSON(host, site, list, accessToken, clazz);
        List<T> completeList = new ArrayList<>(listResponse.getValue());
        while (listResponse.getNextLink() != null && !listResponse.getNextLink().isEmpty()) {
            listResponse = getListItemsJSON(listResponse.getNextLink(), accessToken, clazz);
            completeList.addAll(listResponse.getValue());
        }
        return completeList;
    }

    String getListItemsURI(String host, String site, String list) {
        return String.format("https://%s/sites/%s/_api/Web/Lists/GetByTitle('%s')/Items", host, site, list);
    }

    public <T> GetListResponse<T> getListItemsJSON(String host, String site, String list, String accessToken, Class<T> clazz) throws IOException {
        return getListItemsJSON(getListItemsURI(host, site, list), accessToken, clazz);
    }

    public <T> GetListResponse<T> getListItemsJSON(String uri, String accessToken, Class<T> clazz) throws IOException {
        try (CloseableHttpResponse response = getListItemsJSONResponse(uri, accessToken)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException(String.format("Expected status code 200, found %d", response.getStatusLine().getStatusCode()));
            }
            return getReader().readValue(response.getEntity().getContent(), getReader().getTypeFactory().constructParametricType(GetListResponse.class, clazz));
        } catch (Exception e) {
            throw new IOException("Error reading List into " + clazz.getSimpleName(), e);
        }
    }

    public CloseableHttpResponse getListItemsJSONResponse(String host, String site, String list, String accessToken) throws IOException {
        return getListItemsJSONResponse(getListItemsURI(host, site, list), accessToken);
    }

    public CloseableHttpResponse getListItemsJSONResponse(String uri, String accessToken) throws IOException {
        HttpGet request = new HttpGet(uri);
        request.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken));
        request.setHeader(HttpHeaders.ACCEPT, "application/json;odata=nometadata");
        try {
            logger.info("GET /" + uri);
            return getClient().execute(request);
        } catch (Exception e) {
            throw new IOException("Error getting List by Name GET /" + uri, e);
        }
    }

    CloseableHttpClient getClient() {
        return client;
    }

    ObjectMapper getReader() {
        return reader;
    }
}