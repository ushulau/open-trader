package com.gplex.open.trader.domain.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad S. on 11/12/17.
 */
public class Level2UpdateResponse {
    @JsonProperty("type")
    private String type;
    @JsonProperty("product_id")
    private String productId;
    @JsonProperty("changes")
    private List<List<String>> changes;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("product_id")
    public String getProductId() {
        return productId;
    }

    @JsonProperty("product_id")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @JsonProperty("changes")
    public List<List<String>> getChanges() {
        return changes;
    }

    @JsonProperty("changes")
    public void setChanges(List<List<String>> changes) {
        this.changes = changes;
    }

    public Level2UpdateResponse() {
        this.changes = new ArrayList<>();
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
