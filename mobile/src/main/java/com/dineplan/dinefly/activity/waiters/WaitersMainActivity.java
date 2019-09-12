package com.dineplan.dinefly.activity.waiters;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.waiters.RestaurantTablesPager;
import com.dineplan.dinefly.component.waiters.WaitersSidebar;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import com.dineplan.dinefly.dialog.waiters.WaitersAccessConfirmationDialog;
import com.dineplan.dinefly.logic.commons.SettingsStatusPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterTablesPresenter;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;

import java.util.List;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
public class WaitersMainActivity extends WaitersBaseActivity implements WaitersSidebar.Callback, RestaurantTablesPager.Callback, SwipeRefreshLayout.OnRefreshListener, WaiterTablesPresenter.View, SettingsStatusPresenter.View
{

    @BindView(R.id.waitersToolbar)
    Toolbar toolbar;

    WaitersSidebar sidebar;

    @BindView(R.id.waitersMainSectionTables)
    RestaurantTablesPager tablesAndHalls;

    @BindView(R.id.waitersCrossfadeContent)
    SwipeRefreshLayout refresher;

    TextView busyCounterView;

    TextView vacantCounterView;

    @InjectPresenter
    WaiterTablesPresenter tablesPresenter;

    @InjectPresenter
    SettingsStatusPresenter settingsStatusPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (checkAuthorisation())
        {
            setContentView(R.layout.activity_waiters_main);
            configure();
            onRefresh();
        }
    }

    private void configure()
    {
        sidebar = new WaitersSidebar(this, toolbar, this);

        toolbar.inflateMenu(R.menu.waiters_screen_main);
        busyCounterView = toolbar.getMenu().findItem(R.id.menu_main_busy).getActionView().findViewById(R.id.icon_badge_busy);
        vacantCounterView = toolbar.getMenu().findItem(R.id.menu_main_busy).getActionView().findViewById(R.id.icon_badge_vacant);

        refresher.setColorSchemeResources(R.color.color_primary, R.color.color_light_blue, R.color.material_light_green_800);
        refresher.setOnRefreshListener(this);

        tablesAndHalls.setCallback(this);
        tablesAndHalls.setSwipeToRefreshComponent(refresher);
        tablesAndHalls.setData(App.getDataManager().getWaiterDataManage().getHalls());

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            public void onClick(final View view)
            {
                sidebar.toggle();
            }
        });
    }


    public void onSidebarMenuLogoff()
    {
        if (App.getSettings().isLogoutLockActive())
        {
            new WaitersAccessConfirmationDialog(this).show(new WaitersAccessConfirmationDialog.Callback()
            {
                public void onAuthConfirmed()
                {
                    doLogoff();
                }

                public void onAuthFailed()
                {

                }
            });
        } else
        {
            doLogoff();
        }
    }

    public void onSidebarMenuSettings()
    {
        if (App.getSettings().isSettingsLockActive())
        {
            new WaitersAccessConfirmationDialog(this).show(new WaitersAccessConfirmationDialog.Callback()
            {
                public void onAuthConfirmed()
                {
                    doOpenSettings();
                }

                public void onAuthFailed()
                {

                }
            });
        } else
        {
            doOpenSettings();
        }
    }

    private void doLogoff()
    {
        App.getDataManager().getWaiterDataManage().logoff();
        quitToMainAcreen();
    }

    private void doOpenSettings()
    {
        Bundler.waitersSettingsActivity().start(this);
    }

    public void onRestaurantHallOpened(final int index, final WaiterRoom hall)
    {
        final int vacant = hall.getVacantTablesCount();
        final int busy = hall.getBusyTablesCount();

        busyCounterView.setText(String.valueOf(busy));
        vacantCounterView.setText(String.valueOf(vacant));
        toolbar.setTitle(hall.getName());
        toolbar.invalidate();
    }

    public void onRestaurantTableSelected(final WaiterTable table, final boolean viaLongTap)
    {
        if (App.getSettings().isTableLockActive())
        {
            new WaitersAccessConfirmationDialog(this).show(new WaitersAccessConfirmationDialog.Callback()
            {
                public void onAuthConfirmed()
                {
                    doOpenTable(table, viaLongTap);
                }

                public void onAuthFailed()
                {

                }
            });
        } else
        {
            doOpenTable(table, viaLongTap);
        }
    }

    private void doOpenTable(WaiterTable table, boolean viaLongTap)
    {
        Bundler.waitersTableActivity(table.getTableId(), viaLongTap).start(this);
    }

    public void onRefresh()
    {
        refresher.setRefreshing(true);
        tablesPresenter.updateTablesStatus();
    }

    public void onTablesStatusUpdated(final List<WaiterRoom> tables)
    {
        refresher.setRefreshing(false);
        tablesAndHalls.setData(tables);
    }

    public void pnTablesStatusUpdateError(final Throwable err)
    {
        refresher.setRefreshing(false);
        showError(err);
    }

    public void onEmptyTablesModeOn()
    {
        finish();
        App.getContext().startActivity(Bundler.waitersMenuActivity(0).intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public void onSettingsChanged()
    {
        tablesAndHalls.applySettings();
        onRefresh();
    }
}
