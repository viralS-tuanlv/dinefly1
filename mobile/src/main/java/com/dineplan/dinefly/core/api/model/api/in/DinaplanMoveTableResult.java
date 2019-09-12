package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 07/10/2017
 */
public class DinaplanMoveTableResult
{

    @SerializedName("isError")
    private boolean error;

    @SerializedName(value = "error", alternate = "Error")
    private String errorMessage;

    public DinaplanMoveTableResult()
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

}
