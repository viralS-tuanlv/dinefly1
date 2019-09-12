package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DineplanGuestQuestionnarie
{
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("questions")
    List<FeedbackQuestion> questions = new ArrayList<>();

    public DineplanGuestQuestionnarie()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<FeedbackQuestion> getQuestions()
    {
        return questions;
    }

    public void setQuestions(List<FeedbackQuestion> questions)
    {
        this.questions = questions;
    }
}
