package com.dineplan.dinefly.logic.waiters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.err.DineflyException;
import eu.livotov.labs.android.robotools.os.RTAsyncTask;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 17/10/2017
 */
@InjectViewState
public class WaiterMenuSyncPresenter extends MvpPresenter<WaiterMenuSyncPresenter.View>
{

    public void syncMenu()
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().syncMenu(true);
            }

            protected void onPostExecute()
            {
                getViewState().onMenuSynced();
            }

            protected void onError(final Throwable t)
            {
                getViewState().onMenuSyncError(new DineflyException(t));
            }
        }.execPool();
    }

    @StateStrategyType(AddToEndStrategy.class)
    public interface View extends MvpView
    {

        void onMenuSyncError(final DineflyException e);

        void onMenuSynced();
    }
}
