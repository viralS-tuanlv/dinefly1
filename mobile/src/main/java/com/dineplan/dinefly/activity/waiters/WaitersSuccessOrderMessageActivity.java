package com.dineplan.dinefly.activity.waiters;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.feedback.FeedbackView;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.api.model.api.in.DineplanGuestQuestionnarie;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder;
import com.dineplan.dinefly.logic.waiters.WaiterCompletedOrderPresenter;
import com.google.gson.JsonSyntaxException;

import java.util.List;

import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
public class WaitersSuccessOrderMessageActivity extends WaitersBaseActivity implements WaiterCompletedOrderPresenter.View, FeedbackView.Callback
{

    @Arg
    long ticketId;

    @BindView(R.id.successLogo)
    ImageView successLogo;

    @BindView(R.id.ticketNumber)
    TextView ticketNumber;

    @BindView(R.id.btnReprint)
    View btnReprint;

    @BindView(R.id.orderFeedback)
    FeedbackView feedbackView;

    @InjectPresenter
    WaiterCompletedOrderPresenter orderPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundler.inject(this);

        if (checkProvision())
        {
            setContentView(R.layout.activity_waiters_order_success);
            configure();
            orderPresenter.loadOrder(ticketId);
        } else
        {
            finish();
        }
    }

    private void configure()
    {
        btnReprint.setVisibility(App.getDataManager().getWaiterDataManage().getConfiguration().isCanPrintLocalBolls() ? View.VISIBLE : View.GONE);
        feedbackView.setCallback(this);
    }

    @OnClick(R.id.btnReprint)
    void onReprintClick()
    {
        showBlockingIndeterminateProgressDialog(false, null, getString(R.string.please_wait));
        orderPresenter.printOrderLocally(ticketId);
    }

    @OnClick(R.id.btnClose)
    void onCloseClick()
    {
        finish();
    }

    public void onOrderLoaded(final WaiterOrder order, DineplanGuestQuestionnarie feedback)
    {
        ticketNumber.setText(getString(R.string.order_number, order.getDineplanOrderId()));

        if (feedback!=null)
        {
            feedbackView.setVisibility(View.VISIBLE);
            feedbackView.setData(feedback.getQuestions());
        }
    }

    public void onOrderNotFound(final long id, DineplanGuestQuestionnarie feedback)
    {
        ticketNumber.setText(getString(R.string.order_number, id));

        if (feedback!=null)
        {
            feedbackView.setVisibility(View.VISIBLE);
            feedbackView.setData(feedback.getQuestions());
        }
    }

    public void onError(final Throwable t)
    {
        hideBlockingProgressDialog();

        if (t instanceof JsonSyntaxException)
        {
            showMessage(R.string.feedback_no_configured, new DialogCloseCallback()
            {
                @Override
                public void onDialogClosed()
                {
                    finish();
                }
            });
        } else
        {
            showError(t, new DialogCloseCallback()
            {
                @Override
                public void onDialogClosed()
                {
                    finish();
                }
            });
        }
    }

    public void onOrderReprinted()
    {
        hideBlockingProgressDialog();
        App.showToast(R.string.order_reprinted, false);
    }

    @Override
    public void onFeedbackSent()
    {
        hideBlockingProgressDialog();
        feedbackView.setVisibility(View.GONE);
    }

    @Override
    public void onFeedbackFailed(Throwable err)
    {
        hideBlockingProgressDialog();
        feedbackView.setVisibility(View.GONE);
    }

    @Override
    public void onRatingReady(List<GuestFeedbackEntry> answers)
    {
        showBlockingIndeterminateProgressDialog(false, null, getString(R.string.sending_fedback));
        orderPresenter.sendFeedback(ticketId, answers);
    }
}
