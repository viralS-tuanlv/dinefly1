package com.dineplan.dinefly.logic.commons;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.event.SettingsChangedEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */

@InjectViewState
public class SettingsStatusPresenter extends MvpPresenter<SettingsStatusPresenter.View>
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

    class Subscriber
    {

        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onSettingsChangedEvent(SettingsChangedEvent event)
        {
            getViewState().onSettingsChanged();
        }
    }

    @StateStrategyType(AddToEndStrategy.class)
    public interface View extends MvpView
    {

        void onSettingsChanged();
    }
}
