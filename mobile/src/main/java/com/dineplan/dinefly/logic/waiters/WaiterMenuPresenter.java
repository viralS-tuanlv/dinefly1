package com.dineplan.dinefly.logic.waiters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import eu.livotov.labs.android.robotools.os.RTAsyncTask;

import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@InjectViewState
public class WaiterMenuPresenter extends MvpPresenter<WaiterMenuPresenter.View>
{
    public List<WaiterMenuCategory> getMenu()
    {
        return App.getDataManager().getWaiterDataManage().getMenu();
    }

    public WaiterMenuCategory getMenuItemCategory(int categoryId)
    {
        return App.getDataManager().getWaiterDataManage().findCategory(categoryId);
    }

    public List<WaiterMenuItem> queryMenu(final String query)
    {
        return App.getDataManager().getWaiterDataManage().searchMenu(query);
    }

    public WaiterMenuItem getMenuItem(final long menuItemId)
    {
        return App.getDataManager().getWaiterDataManage().findItem(menuItemId);
    }

    @StateStrategyType(SkipStrategy.class)
    public interface View extends MvpView
    {
    }
}
