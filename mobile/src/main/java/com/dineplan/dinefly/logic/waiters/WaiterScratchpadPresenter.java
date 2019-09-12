package com.dineplan.dinefly.logic.waiters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@InjectViewState
public class WaiterScratchpadPresenter extends MvpPresenter<WaiterScratchpadPresenter.View>
{
    public WaiterMenuItem menuItem;
    public WaiterMenuItem selectedMenuItem;
    public List<WaiterMenuItem> menuItems = new ArrayList<>();

    @StateStrategyType(SkipStrategy.class)
    public interface View extends MvpView
    {
    }
}
