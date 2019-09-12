package com.dineplan.dinefly.logic.waiters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.crashlytics.android.Crashlytics;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom;
import com.dineplan.dinefly.event.TablesChangedEvent;
import eu.livotov.labs.android.robotools.os.RTAsyncTask;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@InjectViewState
public class WaiterTablesPresenter extends MvpPresenter<WaiterTablesPresenter.View>
{
    Subscriber subscriber = new Subscriber();

    protected void onFirstViewAttach()
    {
        super.onFirstViewAttach();
        App.subscribe(subscriber);
    }

    public void onDestroy()
    {
        App.unsubscribe(subscriber);
        super.onDestroy();
    }

    public List<WaiterRoom> getTables()
    {
        return App.getDataManager().getWaiterDataManage().getHalls();
    }

    public void updateTablesStatus()
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().syncTables();
            }

            protected void onPostExecute()
            {
                final List<WaiterRoom> halls = App.getDataManager().getWaiterDataManage().getHalls();
                int tableCount = 0;

                for (WaiterRoom room : halls)
                {
                    tableCount+=room.getTables().size();
                }

                if (tableCount>0)
                {
                    getViewState().onTablesStatusUpdated(halls);
                } else
                {
                    getViewState().onEmptyTablesModeOn();
                }
            }

            protected void onError(final Throwable t)
            {
                Crashlytics.logException(t);
                getViewState().pnTablesStatusUpdateError(t);
            }
        }.execPool();
    }

    class Subscriber
    {
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onTablesUpdated(TablesChangedEvent event)
        {
            getViewState().onTablesStatusUpdated(App.getDataManager().getWaiterDataManage().getHalls());
        }
    }

    @StateStrategyType(AddToEndStrategy.class)
    public interface View extends MvpView
    {

        void onTablesStatusUpdated(List<WaiterRoom> tables);

        void pnTablesStatusUpdateError(Throwable err);

        void onEmptyTablesModeOn();
    }
}
