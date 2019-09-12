package com.dineplan.dinefly.component.feedback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.api.model.api.in.FeedbackQuestion;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class QuestionRatingView extends FrameLayout implements QuestionView, RatingBar.OnRatingBarChangeListener
{
    @BindView(R.id.entryName)
    TextView textQuestion;

    @BindView(R.id.entryRatingBar)
    MaterialRatingBar ratingAnswer;

    GuestFeedbackEntry answer = new GuestFeedbackEntry();
    FeedbackQuestion question;

    public QuestionRatingView(@NonNull Context context)
    {
        super(context);
        configure();
    }

    public QuestionRatingView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public QuestionRatingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    public QuestionRatingView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_feedback_entry_rating, this, true);
        ButterKnife.bind(this);
        ratingAnswer.setOnRatingBarChangeListener(this);
    }

    @Override
    public void setQuestion(FeedbackQuestion question)
    {
        this.question = question;
        answer.setId(question.getId());
        answer.setValue("");
        textQuestion.setText(question.getQuestion());

        try
        {
            final int defaultRating = Integer.parseInt(question.getDefaultAnswer());

            if (defaultRating >= 0 && defaultRating <= 5)
            {
                ratingAnswer.setRating(defaultRating);
            }
        }
        catch (Throwable ignored)
        {

        }
    }

    @Override
    public GuestFeedbackEntry getAnswer()
    {
        return answer;
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser)
    {
        answer.setValue("" + (int) rating);
    }
}
