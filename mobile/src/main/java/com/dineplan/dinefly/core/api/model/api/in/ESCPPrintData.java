package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ESCPPrintData
{

    @SerializedName("contentByte")
    List<byte[]> content = new ArrayList<>();

    public ESCPPrintData()
    {
    }

    public List<byte[]> getContent()
    {
        return content;
    }

    public void setContent(final List<byte[]> content)
    {
        this.content = content;
    }

}
