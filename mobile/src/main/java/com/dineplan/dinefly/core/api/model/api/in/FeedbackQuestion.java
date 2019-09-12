package com.dineplan.dinefly.core.api.model.api.in;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FeedbackQuestion
{
    public final static int TYPE_TEXT = 0;
    public final static int TYPE_OPTION_SINGLE = 1;
    public final static int TYPE_OPTION_MULTIPLE = 2;
    public final static int TYPE_RATING = 3;

    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("question")
    String question;

    @SerializedName("questionType")
    int type;

    @SerializedName("answers")
    String answers;

    @SerializedName("defaultAnswer")
    String defaultAnswer;

    public FeedbackQuestion()
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

    public String getQuestion()
    {
        return question;
    }

    public void setQuestion(String question)
    {
        this.question = question;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getAnswers()
    {
        return answers;
    }

    public void setAnswers(String answers)
    {
        this.answers = answers;
    }

    public String getDefaultAnswer()
    {
        return defaultAnswer;
    }

    public void setDefaultAnswer(String defaultAnswer)
    {
        this.defaultAnswer = defaultAnswer;
    }

    public List<String> getAnswersList()
    {
        Type type = new TypeToken<ArrayList<String>>()
        {
        }.getType();

        try
        {
            final List<String> list = new Gson().fromJson(("" + answers), type);
            return list != null ? list : new ArrayList<String>();
        } catch (Throwable ignored)
        {
        }

        List<String> emptyData = new ArrayList<>();
        emptyData.add("");
        return emptyData;
    }
}
