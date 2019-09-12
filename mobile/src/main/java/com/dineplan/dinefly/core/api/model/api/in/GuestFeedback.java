package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 21/10/2017
 */
public class GuestFeedback
{

    @SerializedName("Group")
    int group;

    @SerializedName("TicketNo")
    String ticket;

    List<GuestFeedbackEntry> answers = new ArrayList<>();

    public GuestFeedback()
    {
    }

    public GuestFeedback(DineplanGuestQuestionnarie questionnarie, List<GuestFeedbackEntry> answers, String ticketId)
    {
        group = questionnarie.id;
        ticket = ticketId;
        this.answers.addAll(answers);
    }

    public int getGroup()
    {
        return group;
    }

    public void setGroup(int group)
    {
        this.group = group;
    }

    public String getTicket()
    {
        return ticket;
    }

    public void setTicket(String ticket)
    {
        this.ticket = ticket;
    }

    public List<GuestFeedbackEntry> getAnswers()
    {
        return answers;
    }

    public void setAnswers(List<GuestFeedbackEntry> answers)
    {
        this.answers = answers;
    }
}
