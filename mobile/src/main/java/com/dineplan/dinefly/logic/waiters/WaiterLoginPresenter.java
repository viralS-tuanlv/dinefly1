package com.dineplan.dinefly.logic.waiters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import eu.livotov.labs.android.robotools.os.RTAsyncTask;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@InjectViewState
public class WaiterLoginPresenter extends MvpPresenter<WaiterLoginPresenter.View>
{

    public void login(final String password)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().authenticate(password);
            }

            protected void onPostExecute()
            {
                if (!App.getDataManager().getWaiterDataManage().getConfiguration().isSalesOpen())
                {
                    App.getDataManager().getWaiterDataManage().logoff();
                    getViewState().onSalesClosedLoginError();
                } else
                {
                    getViewState().onLoginSuccessfull();
                }
            }

            protected void onError(final Throwable t)
            {
                getViewState().onLoginError(t);
            }
        }.execPool();
    }

    public void unlink()
    {
        App.getDataManager().resetDevice();
    }

    @StateStrategyType(SkipStrategy.class)
    public interface View extends MvpView
    {

        void onLoginSuccessfull();

        void onLoginError(Throwable err);

        void onSalesClosedLoginError();
    }
}
