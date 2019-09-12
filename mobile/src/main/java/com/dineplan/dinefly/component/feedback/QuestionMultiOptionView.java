package com.dineplan.dinefly.component.feedback;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.api.model.api.in.FeedbackQuestion;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.livotov.labs.android.robotools.hardware.RTDevice;

public class QuestionMultiOptionView extends FrameLayout implements QuestionView, CompoundButton.OnCheckedChangeListener
{
    @BindView(R.id.entryName)
    TextView textQuestion;

    @BindView(R.id.entryCheckboxGroup)
    LinearLayout entryCheckboxGroup;

    GuestFeedbackEntry answer = new GuestFeedbackEntry();
    FeedbackQuestion question;

    public QuestionMultiOptionView(@NonNull Context context)
    {
        super(context);
        configure();
    }

    public QuestionMultiOptionView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public QuestionMultiOptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    public QuestionMultiOptionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_feedback_entry_multioption, this, true);
        ButterKnife.bind(this);
    }

    @Override
    public void setQuestion(FeedbackQuestion question)
    {
        this.question = question;
        answer.setId(question.getId());
        answer.setValue("");
        textQuestion.setText(question.getQuestion());

        List<String> options = question.getAnswersList();

        for (String option : options)
        {
            AppCompatCheckBox cb = new AppCompatCheckBox(getContext());
            cb.setText(option);
            cb.setTag(option);
            cb.setTextColor(Color.BLACK);
            cb.setOnCheckedChangeListener(this);
            cb.setBackgroundResource(R.drawable.bg_checkradiobox_selector);
            cb.setButtonDrawable(R.drawable.bg_checkradiobox_button);
            cb.setMinWidth((int) RTDevice.px2dp(App.getContext(), 200));
            cb.setPadding((int)RTDevice.px2dp(App.getContext(),16),(int)RTDevice.px2dp(App.getContext(),16),(int)RTDevice.px2dp(App.getContext(),16),(int)RTDevice.px2dp(App.getContext(),16));
            entryCheckboxGroup.addView(cb);

            LinearLayout.LayoutParams pp = (LinearLayout.LayoutParams)cb.getLayoutParams();
            pp.bottomMargin = (int) RTDevice.px2dp(App.getContext(), 16);
            cb.setLayoutParams(pp);
        }
    }

    @Override
    public GuestFeedbackEntry getAnswer()
    {
        return answer;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        StringBuilder builder = new StringBuilder();

        for (int i=0;i<entryCheckboxGroup.getChildCount();i++)
        {
            final View view = entryCheckboxGroup.getChildAt(i);

            if (view instanceof CheckBox)
            {
                final CheckBox cb = (CheckBox)view;

                if (cb.isChecked())
                {
                    if (builder.length()>0)
                    {
                        builder.append(",");
                    }

                    builder.append(cb.getTag());
                }
            }
        }

        answer.setValue(builder.toString());
    }
}
