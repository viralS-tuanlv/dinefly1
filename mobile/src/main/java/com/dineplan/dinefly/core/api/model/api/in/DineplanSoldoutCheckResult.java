package com.dineplan.dinefly.core.api.model.api.in;

import com.dineplan.dinefly.core.api.model.api.out.DineplanSoldoutCheckItem;
import com.dineplan.dinefly.core.api.model.api.out.DineplanSoldoutCheckResponse;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 07/10/2017
 */
public class DineplanSoldoutCheckResult
{

    @SerializedName("isError")
    private boolean error;

    @SerializedName(value = "error", alternate = "Error")
    private String errorMessage;

    @SerializedName(value = "response", alternate = "Response")
    private DineplanSoldoutCheckResponse soldOutInfo = new DineplanSoldoutCheckResponse(new ArrayList<DineplanSoldoutCheckItem>());

    public DineplanSoldoutCheckResult()
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

    public DineplanSoldoutCheckResponse getSoldOutInfo()
    {
        return soldOutInfo;
    }

    public void setSoldOutInfo(DineplanSoldoutCheckResponse soldOutInfo)
    {
        this.soldOutInfo = soldOutInfo;
    }
}
