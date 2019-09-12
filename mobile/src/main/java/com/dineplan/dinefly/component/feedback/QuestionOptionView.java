package com.dineplan.dinefly.component.feedback;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.api.model.api.in.FeedbackQuestion;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.livotov.labs.android.robotools.hardware.RTDevice;

public class QuestionOptionView extends FrameLayout implements QuestionView, RadioGroup.OnCheckedChangeListener
{
    @BindView(R.id.entryName)
    TextView textQuestion;

    @BindView(R.id.entryRadioGroup)
    RadioGroup ratingOptionsGroup;

    GuestFeedbackEntry answer = new GuestFeedbackEntry();
    FeedbackQuestion question;

    public QuestionOptionView(@NonNull Context context)
    {
        super(context);
        configure();
    }

    public QuestionOptionView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public QuestionOptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    public QuestionOptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_feedback_entry_option, this, true);
        ButterKnife.bind(this);
        ratingOptionsGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void setQuestion(FeedbackQuestion question)
    {
        this.question = question;
        answer.setId(question.getId());
        answer.setValue("");
        textQuestion.setText(question.getQuestion());

        List<String> options = question.getAnswersList();
        Map<String, Integer> namesToIdsMap = new HashMap<>();

        for (String option : options)
        {
            AppCompatRadioButton rb = new AppCompatRadioButton(getContext());
            rb.setText(option);
            rb.setTag(option);
            rb.setTextColor(Color.BLACK);
            rb.setBackgroundResource(R.drawable.bg_checkradiobox_selector);
            rb.setButtonDrawable(R.drawable.bg_checkradiobox_button);
            rb.setPadding((int)RTDevice.px2dp(App.getContext(),16),(int)RTDevice.px2dp(App.getContext(),16),(int)RTDevice.px2dp(App.getContext(),16),(int)RTDevice.px2dp(App.getContext(),16));
            rb.setMinWidth((int) RTDevice.px2dp(App.getContext(), 200));
            ratingOptionsGroup.addView(rb);
            namesToIdsMap.put(option, rb.getId());

            RadioGroup.LayoutParams pp = (RadioGroup.LayoutParams)rb.getLayoutParams();
            pp.bottomMargin = (int) RTDevice.px2dp(App.getContext(), 16);
            rb.setLayoutParams(pp);
        }

        for (String option : options)
        {
            if (option.equalsIgnoreCase(question.getDefaultAnswer()))
            {
                ratingOptionsGroup.check(namesToIdsMap.get(option));
            }
        }
    }

    @Override
    public GuestFeedbackEntry getAnswer()
    {
        return answer;
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        answer.setValue(group.findViewById(checkedId).getTag().toString());
    }
}
