package com.dineplan.dinefly.core.api.model.api.bill;

import com.dineplan.dinefly.core.api.model.api.in.DineplanGuestQuestionnarie;
import com.google.gson.annotations.SerializedName;

public class BillData {
    @SerializedName("isError")
    private boolean error;

    @SerializedName(value = "response")
    private BillResponse billResponse;


    public boolean isError()
    {
        return error;
    }

    public void setError(final boolean error)
    {
        this.error = error;
    }

    public BillResponse getBillResponse() {
        return billResponse;
    }

    public void setBillResponse(BillResponse billResponse) {
        this.billResponse = billResponse;
    }
}
