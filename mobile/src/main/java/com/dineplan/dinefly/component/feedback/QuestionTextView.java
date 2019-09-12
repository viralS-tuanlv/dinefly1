package com.dineplan.dinefly.component.feedback;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.api.model.api.in.FeedbackQuestion;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionTextView extends FrameLayout implements QuestionView, TextWatcher
{
    @BindView(R.id.entryName)
    TextView textQuestion;

    @BindView(R.id.entryEditText)
    EditText textAnswer;

    GuestFeedbackEntry answer = new GuestFeedbackEntry();
    FeedbackQuestion question;

    public QuestionTextView(@NonNull Context context)
    {
        super(context);
        configure();
    }

    public QuestionTextView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public QuestionTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    public QuestionTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_feedback_entry_text, this, true);
        ButterKnife.bind(this);

        textAnswer.addTextChangedListener(this);
    }

    @Override
    public void setQuestion(FeedbackQuestion question)
    {
        this.question = question;
        answer.setId(question.getId());
        answer.setValue("");
        textQuestion.setText(question.getQuestion());
        textAnswer.setText("");
    }

    @Override
    public GuestFeedbackEntry getAnswer()
    {
        return answer;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        answer.setValue(textAnswer.getText().toString());
    }
}
