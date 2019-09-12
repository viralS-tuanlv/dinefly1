package com.dineplan.dinefly.logic.waiters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.api.model.api.in.DineplanGuestQuestionnarie;
import com.dineplan.dinefly.core.api.model.api.in.GuestFeedbackEntry;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder;
import java.util.List;

import eu.livotov.labs.android.robotools.os.RTAsyncTask;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@InjectViewState
public class WaiterCompletedOrderPresenter extends MvpPresenter<WaiterCompletedOrderPresenter.View>
{
    DineplanGuestQuestionnarie feedback;

    public void loadOrder(final long orderId)
    {
        new RTAsyncTask()
        {
            WaiterOrder order;

            protected void doInBackground() throws Throwable
            {
                order = App.getDataManager().getWaiterDataManage().findOrder(orderId);
                feedback = App.getDataManager().getWaiterDataManage().loadFeedback();
            }

            protected void onPostExecute()
            {
                if (order != null)
                {
                    getViewState().onOrderLoaded(order, feedback);
                } else
                {
                    getViewState().onOrderNotFound(orderId, feedback);
                }
            }

            protected void onError(final Throwable t)
            {
                t.printStackTrace();
                getViewState().onError(t);
            }
        }.execPool();
    }

    public void printOrderLocally(final long orderId)
    {
        new RTAsyncTask()
        {
            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().printOrder(orderId, true);
            }

            protected void onPostExecute()
            {
                getViewState().onOrderReprinted();
            }

            protected void onError(final Throwable t)
            {
                t.printStackTrace();
                getViewState().onError(t);
            }
        }.execPool();
    }

    public void sendFeedback(final long ticketId, final List<GuestFeedbackEntry> answers)
    {
        new RTAsyncTask()
        {
            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().sendFeedback(feedback, ticketId, answers);
            }

            protected void onPostExecute()
            {
                getViewState().onFeedbackSent();
            }

            protected void onError(final Throwable t)
            {
                t.printStackTrace();
                getViewState().onFeedbackFailed(t);
            }
        }.execPool();
    }

    @StateStrategyType(AddToEndStrategy.class)
    public interface View extends MvpView
    {

        void onOrderLoaded(WaiterOrder order, DineplanGuestQuestionnarie feedback);

        void onOrderNotFound(long id, DineplanGuestQuestionnarie feedback);

        void onError(Throwable t);

        void onOrderReprinted();

        void onFeedbackSent();

        void onFeedbackFailed(Throwable err);
    }
}
