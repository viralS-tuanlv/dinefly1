package com.dineplan.dinefly.component.feedback;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.api.model.api.in.FeedbackQuestion;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FeedbackView extends FrameLayout implements ViewPager.OnPageChangeListener
{

    @BindView(R.id.ratingViewRoot)
    ViewGroup ratingRoot;

    @BindView(R.id.ratingPager)
    ViewPager ratingPager;

    @BindView(R.id.questionCounter)
    TextView questionCounter;

    @BindView(R.id.fSubmit)
    Button btnNextDone;

    Callback callback;
    RatingPagerAdapter adapter;

    public FeedbackView(Context context)
    {
        super(context);
        configure(null);
    }

    public FeedbackView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        configure(attrs);
    }

    public FeedbackView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure(attrs);
    }

    @TargetApi(21)
    public FeedbackView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure(attrs);
    }

    private void configure(AttributeSet attrs)
    {
        LayoutInflater.from(getContext()).inflate(R.layout.view_feedback, this, true);
        ButterKnife.bind(this);
        ratingPager.setOffscreenPageLimit(100);
        ratingPager.addOnPageChangeListener(this);
    }

    public Callback getCallback()
    {
        return callback;
    }

    public void setCallback(Callback callback)
    {
        this.callback = callback;
    }

    @OnClick(R.id.fSubmit)
    void onSubmitClick()
    {
        if (adapter != null)
        {
            if (ratingPager.getCurrentItem() < adapter.getCount() - 1)
            {
                ratingPager.setCurrentItem(ratingPager.getCurrentItem() + 1, true);
            }
            else if (callback != null)
            {
                callback.onRatingReady(collectAnswers());
            }
        }
    }

    private List<GuestFeedbackEntry> collectAnswers()
    {
        List<GuestFeedbackEntry> answers = new ArrayList<>();

        for (int i = 0; i < adapter.getCount(); i++)
        {
            answers.add(adapter.getQuestionView(i).getAnswer());
        }

        return answers;
    }

    public void setData(final List<FeedbackQuestion> questions)
    {
        adapter = new RatingPagerAdapter(getContext(), questions);
        ratingPager.setAdapter(adapter);
        onPageSelected(0);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {
        questionCounter.setText(getContext().getString(R.string.rating_question_step, position + 1, adapter.getCount()));
        btnNextDone.setText((position == adapter.getCount() - 1) ? R.string.submit_rating : R.string.submit_rating_next);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    public interface Callback
    {
        void onRatingReady(List<GuestFeedbackEntry> answers);
    }

}