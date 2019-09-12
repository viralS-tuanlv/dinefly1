package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

public class DineplanGuestQuestionnarieResponse
{
    @SerializedName("isError")
    private boolean error;

    @SerializedName(value = "error", alternate = "Error")
    private String errorMessage;

    @SerializedName(value = "response", alternate = "Response")
    private DineplanGuestQuestionnarie questionnarie;

    public DineplanGuestQuestionnarieResponse()
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

    public DineplanGuestQuestionnarie getQuestionnarie()
    {
        return questionnarie;
    }

    public void setQuestionnarie(DineplanGuestQuestionnarie questionnarie)
    {
        this.questionnarie = questionnarie;
    }
}
