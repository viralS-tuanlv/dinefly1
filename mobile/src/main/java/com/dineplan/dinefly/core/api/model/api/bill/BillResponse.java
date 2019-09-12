package com.dineplan.dinefly.core.api.model.api.bill;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BillResponse {
    @SerializedName("content")
    private List<String> content;

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
