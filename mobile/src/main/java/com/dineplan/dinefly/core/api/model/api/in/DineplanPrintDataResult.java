package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 07/10/2017
 */
public class DineplanPrintDataResult
{

    @SerializedName("isError")
    private boolean error;

    @SerializedName(value = "error", alternate = "Error")
    private String errorMessage;

    @SerializedName(value = "response", alternate = "Response")
    private ESCPPrintData data;

    public DineplanPrintDataResult()
    {
    }

    public boolean isError()
    {
        return error;
    }

    public void setError(final boolean error)
    {
        this.error = error;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(final String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public ESCPPrintData getData()
    {
        return data;
    }

    public void setData(final ESCPPrintData data)
    {
        this.data = data;
    }
}
