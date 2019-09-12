package com.dineplan.dinefly.logic.provision;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import eu.livotov.labs.android.robotools.os.RTAsyncTask;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */

@InjectViewState
public class ProvisioningPresenter extends MvpPresenter<ProvisioningPresenter.View>
{

    public void provision(final String data)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().provision(data);
            }

            protected void onPostExecute()
            {
                getViewState().onProvisionCompleted();
            }

            protected void onError(final Throwable t)
            {
                getViewState().onProvisionFailed();
            }
        }.execPool();
    }

    @StateStrategyType(SkipStrategy.class)
    public interface View extends MvpView
    {
        void onProvisionFailed();

        void onProvisionCompleted();
    }
}
