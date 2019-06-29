package com.philipbui.sharepoint.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GetListResponse<T> {

    private final String nextLink;
    private final List<T> value;

    @JsonCreator
    public GetListResponse(@JsonProperty("odata.nextLink") String nextLink, @JsonProperty("value") List<T> value) {
        this.nextLink = nextLink;
        this.value = value;
    }

    public String getNextLink() {
        return nextLink;
    }

    public List<T> getValue() {
        return value;
    }
}
