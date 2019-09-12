package com.dineplan.dinefly.component.feedback;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.dineplan.dinefly.core.api.model.api.in.FeedbackQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatingPagerAdapter extends PagerAdapter
{
    private final Context context;
    List<FeedbackQuestion> questions = new ArrayList<>();
    Map<Integer,QuestionView> views = new HashMap<>();

    public RatingPagerAdapter(Context ctx, List<FeedbackQuestion> questions)
    {
        this.context = ctx;
        this.questions.addAll(questions);
    }

    @Override
    public int getCount()
    {
        return questions.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        QuestionView view = views.get(position);

        if (view == null)
        {
            final int qt = questions.get(position).getType();

            switch (qt)
            {
                case FeedbackQuestion.TYPE_TEXT:
                    view = new QuestionTextView(context);
                    break;

                case FeedbackQuestion.TYPE_OPTION_SINGLE:
                    view = new QuestionOptionView(context);
                    break;

                case FeedbackQuestion.TYPE_OPTION_MULTIPLE:
                    view = new QuestionMultiOptionView(context);
                    break;

                case FeedbackQuestion.TYPE_RATING:
                    view = new QuestionRatingView(context);
                    break;

                default:
                    throw new IllegalArgumentException("Unknown feedback question type: " + qt);
            }

            view.setQuestion(questions.get(position));
            views.put(position, view);
        }

        container.addView((View) view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object)
    {
        return view == object;
    }

    public QuestionView getQuestionView(int idx)
    {
        return views.get(idx);
    }
}
