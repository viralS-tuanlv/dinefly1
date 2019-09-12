package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

public class GuestFeedbackEntry
{
    @SerializedName("Question")
    int id;

    @SerializedName("Answer")
    String value;

    public GuestFeedbackEntry()
    {
    }

    public GuestFeedbackEntry(FeedbackQuestion question, int selectedIndex)
    {
        id = question.id;

        try
        {
            value = question.getAnswersList().get(selectedIndex);
        } catch (Throwable err)
        {
            value = question.defaultAnswer;
        }
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}
