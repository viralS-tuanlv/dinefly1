package com.dineplan.dinefly.component.feedback;

import com.dineplan.dinefly.core.api.model.api.in.FeedbackQuestion;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;

interface QuestionView
{
    void setQuestion(FeedbackQuestion question);
    GuestFeedbackEntry getAnswer();
}
