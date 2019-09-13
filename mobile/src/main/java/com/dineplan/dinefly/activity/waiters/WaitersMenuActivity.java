package com.dineplan.dinefly.activity.waiters;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.waiters.CategoriesStripView;
import com.dineplan.dinefly.component.waiters.CategoriesTabStripView;
import com.dineplan.dinefly.component.waiters.MenuCategoryListingPager;
import com.dineplan.dinefly.component.waiters.MenuGrid;
import com.dineplan.dinefly.component.waiters.PreoderSidebar;
import com.dineplan.dinefly.component.waiters.SubtagsSelectionDialog;
import com.dineplan.dinefly.component.waiters.TicketOrdersList;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.err.DineflyException;
import com.dineplan.dinefly.data.MenuDisplayMode;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuCategory;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import com.dineplan.dinefly.data.model.waiters.WaiterOrder;
import com.dineplan.dinefly.data.model.waiters.WaiterOrderSaleItem;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import com.dineplan.dinefly.dialog.waiters.PaxDataDialog;
import com.dineplan.dinefly.logic.waiters.WaiterMenuPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterMenuSyncPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterOrderPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterScratchpadPresenter;
import com.dineplan.dinefly.logic.waiters.WaiterTablePresenter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.transitionseverywhere.Fade;
import com.transitionseverywhere.TransitionManager;
import com.transitionseverywhere.TransitionSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;
import in.workarounds.bundler.annotations.Required;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
public class WaitersMenuActivity extends WaitersBaseActivity implements WaiterTablePresenter.View, WaiterMenuSyncPresenter.View, WaiterMenuPresenter.View, WaiterOrderPresenter.View, WaiterScratchpadPresenter.View
{

    @Arg
    @Required
    long tableId;

    @BindView(R.id.waitersMenuScreenRoot)
    ViewGroup screenRoot;

    @BindView(R.id.waitersToolbar)
    Toolbar toolbar;

//    @BindView(R.id.waiterMenuCatsTabs)
//    @Nullable
    CategoriesTabStripView catsTabsView;

    @BindView(R.id.waiterMenuCatsLIst)
    @Nullable
    CategoriesStripView catsListView;

    @BindView(R.id.preOrderList)
    @Nullable
    TicketOrdersList preorderList; // phone mode only

    @BindView(R.id.orderPanel)
    @Nullable
    View preorderPanel; // tablet-land only

    @BindView(R.id.waiterMenuList)
    MenuCategoryListingPager menuView;

    @BindView(R.id.waiterMenuSearchResults)
    MenuGrid searchResults;

    @Nullable
    TextView cartBadge;

    @BindView(R.id.search_view)
    MaterialSearchView searchView;

    @BindView(R.id.waiterMenuRefresher)
    SwipeRefreshLayout refresher;

    @BindView(R.id.waitersBtnPreorderSubmit)
    @Nullable
    View btnSubmit;

    @BindView(R.id.waitersBtnPreorderClear)
    @Nullable
    View btnClear;

    @InjectPresenter
    WaiterTablePresenter tablePresenter;

    @InjectPresenter
    WaiterMenuSyncPresenter menuSyncPresenter;

    @InjectPresenter
    WaiterMenuPresenter menuPresenter;

    @InjectPresenter
    WaiterOrderPresenter orderPresenter;

    @InjectPresenter(type = PresenterType.GLOBAL, tag = "fly")
    WaiterScratchpadPresenter scratchpadPresenter;

    PreoderSidebar sidebar;
    static volatile String orderComments;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundler.inject(this);

        if (checkAuthorisation())
        {
            setContentView(R.layout.activity_waiters_menu);
            setResult(RESULT_CANCELED);
            configure();
            showData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            editItem(scratchpadPresenter.selectedMenuItem);
        }
    }

    public void onBackPressed()
    {
        if (searchView.isSearchOpen())
        {
            searchView.closeSearch();
        }
        else if (sidebar != null && sidebar.hasPreorders())
        {
            if (sidebar.isOpen())
            {
                showToast(R.string.unconfirmed_preorders_on_menu_exit_warning, false);
            }
            else
            {
                sidebar.open();
            }
        }
        else
        {
            quitScreen();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }

    private void showData()
    {
        ///do logger to test
        final WaiterTable table = tablePresenter.getTable(tableId);

        if (table != null)
        {
            showToolbarData(table);
            showMenu();
            showUpdatedOrder(table);
        }
        else
        {
            quitScreen();
        }
    }

    private void quitScreen()
    {
        if (tableId == 0)
        {
            App.getDataManager().getWaiterDataManage().logoff();
            finish();
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void showMenu()
    {
        final List<WaiterMenuCategory> menu = App.getDataManager().getWaiterDataManage().getMenu();
        menuView.setData(menu);

        if (catsTabsView!=null) {
            catsTabsView.setData(menuView);
        }

        if (catsListView!=null) catsListView.setData(menu);
    }

    private void showToolbarData(final WaiterTable table)
    {
        if (table.isVirtualTable())
        {
            toolbar.setTitle(R.string.quick_order_mode);
            toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        }
        else
        {
            toolbar.setTitle(getString(R.string.table_details_toolbar_title, table.getName()));
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        }
    }

    private void configure()
    {
        final boolean sidebarPreorderMode = (!App.isTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);

        if (sidebarPreorderMode)
        {
            sidebar = new PreoderSidebar(this, new PreoderSidebar.Callback()
            {
                public void onClearPreorder()
                {
                    onClearPreoderClick();
                }

                public void onSubmitPreorder()
                {
                    onSubmitPreorderClick();
                }

                public void onRemovePreorderItemFromDrawer(final WaiterOrderSaleItem item)
                {
                    orderPresenter.removePreorderedItem(item);
                }

                public void onEditPreorderedItem(final WaiterOrderSaleItem item)
                {
                    if (item.getComboItems().size() > 0)
                    {
                        Bundler.waitersComboChooserActivity(tableId, item.getMenuItemid()).orderItemId(item.getId()).start(WaitersMenuActivity.this);
                    }
                    else
                    {
                        Bundler.waitersPortionChooserActivity(tableId, item.getMenuItemid()).orderItemId(item.getId()).start(WaitersMenuActivity.this);
                    }
                }

                public void onMoveAllPreorderedItemsToServeLaterStatus()
                {
                    orderPresenter.moveAllToSaveLater(tableId);
                }

                @Override
                public void onEditPreorderNotes()
                {
                    editPreorderNote();
                }
            });

            toolbar.inflateMenu(R.menu.waiters_screen_menu_withsidebar);
            toolbar.getMenu().findItem(R.id.menu_cart).getActionView().setOnClickListener(new View.OnClickListener()
            {
                public void onClick(final View view)
                {
                    openPreorderSidebar();
                }
            });
            cartBadge = (TextView) toolbar.getMenu().findItem(R.id.menu_cart).getActionView().findViewById(R.id.icon_badge);
        }
        else
        {
            toolbar.inflateMenu(R.menu.waiters_screen_menu);
        }

        if (catsTabsView != null)
        {
            catsTabsView.setData(menuView);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            public void onClick(final View view)
            {
                onBackPressed();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener()
        {
            public boolean onMenuItemClick(final MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.menu_search:
                        openSearchMode();
                        return true;

                    case R.id.menu_cart:
                        openPreorderSidebar();
                        return true;

                    case R.id.menu_toggle_mode:
                        toggleDisplayMode();
                        return true;
                }

                return false;
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener()
        {
            public boolean onQueryTextSubmit(final String query)
            {
                showFilteredMenu(query);
                return true;
            }

            public boolean onQueryTextChange(final String query)
            {
                showFilteredMenu(query);
                return true;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener()
        {
            public void onSearchViewShown()
            {
                if (catsTabsView != null)
                {
                    catsTabsView.setVisibility(View.GONE);
                }

                searchResults.setData(new ArrayList<WaiterMenuItem>(), false);
                searchResults.setVisibility(View.VISIBLE);
                menuView.setVisibility(View.GONE);
                refresher.setVisibility(View.GONE);
            }

            public void onSearchViewClosed()
            {
                if (catsTabsView!=null) catsTabsView.setVisibility(View.VISIBLE);
                searchResults.setData(new ArrayList<WaiterMenuItem>(), false);
                searchResults.setVisibility(View.GONE);
                menuView.setVisibility(View.VISIBLE);
                refresher.setVisibility(View.VISIBLE);
                showData();
            }
        });

        searchResults.setCallback(new MenuGrid.Callback()
        {
            public void onTableSelected(final WaiterMenuItem item, List<WaiterMenuItem> tagData)
            {
                processmenuItemClick(item, tagData);
            }
        });

        if (catsTabsView!=null) catsTabsView.setCallback(new CategoriesTabStripView.Callback()
        {
            public void onCategorySelected(final WaiterMenuCategory cat)
            {
                menuView.openCategory(cat);
            }
        });

        if (catsListView!=null) catsListView.setCallback(new CategoriesStripView.Callback() {
            @Override
            public void onCategorySelected(WaiterMenuCategory cat) {
                menuView.openCategory(cat);
            }
        });

        menuView.setCallback(new MenuCategoryListingPager.Callback()
        {
            public void onCategoryOpened(final int index, final WaiterMenuCategory category)
            {
                if (catsListView!=null) catsListView.selectCategory(category);
            }

            public void onMenuItemSelected(final WaiterMenuItem item, List<WaiterMenuItem> tagData)
            {
                processmenuItemClick(item, tagData);
            }
        });

        if (preorderList != null)
        {
            preorderList.setCallback(new TicketOrdersList.Callback()
            {
                public void onRemovePreorderedItem(final WaiterOrderSaleItem item)
                {
                    orderPresenter.removePreorderedItem(item);
                }

                public void onHeaderButtonClick(final Object tag)
                {
                    orderPresenter.moveAllToSaveLater(tableId);
                }

                public void onEditTicketNotes(final WaiterOrder ticket)
                {

                }

                public void onEditPreorderedItem(final WaiterOrderSaleItem item)
                {
                    if (item.getComboItems().size() > 0)
                    {
                        App.getContext().startActivity(Bundler.waitersComboChooserActivity(tableId, item.getMenuItemid()).orderItemId(item.getId()).intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                    else
                    {
                        App.getContext().startActivity(Bundler.waitersPortionChooserActivity(tableId, item.getMenuItemid()).orderItemId(item.getId()).intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            });
        }

        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            public void onRefresh()
            {
                menuSyncPresenter.syncMenu();
            }
        });

        menuView.setSwipeToRefreshComponent(refresher);
    }

    private void processmenuItemClick(final WaiterMenuItem item, final List<WaiterMenuItem> tagData)
    {
        if (tagData.size() > 0)
        {
            scratchpadPresenter.menuItem = item;
            scratchpadPresenter.menuItems.clear();
            scratchpadPresenter.menuItems.addAll(tagData);
            startActivityForResult(new Intent(this, WaitersSubtagSelectionActivity.class), 123);
        }
        else
        {
            editItem(item);
        }
    }

    @Optional
    @OnClick(R.id.btnNotes)
    void editPreorderNote()
    {
        new MaterialDialog.Builder(this).title(R.string.order_comments).input(getString(R.string.type_order_comments), orderComments, true, new MaterialDialog.InputCallback()
        {
            @Override
            public void onInput(@NonNull MaterialDialog dialog, CharSequence input)
            {
                orderComments = input.toString();
            }
        }).show();
    }

    private void toggleDisplayMode()
    {
        final MenuDisplayMode displayMode = App.getSettings().getMenuDisplayMode();

        switch (displayMode)
        {
            case GridRich:
                App.getSettings().setMenuDisplayMode(MenuDisplayMode.PlainText);
                break;

            case PlainText:
                App.getSettings().setMenuDisplayMode(MenuDisplayMode.GridRich);
                break;
        }

        finish();
        App.getContext().startActivity(Bundler.waitersMenuActivity(tableId).intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void openPreorderSidebar()
    {
        if (sidebar != null)
        {
            sidebar.open();
        }
    }

    private void showFilteredMenu(final String query)
    {
        searchResults.setData(menuPresenter.queryMenu(query), false);
    }

    private void openSearchMode()
    {
        searchView.showSearch(true);
    }

    private void editItem(final WaiterMenuItem item)
    {
        if (item.checkIfCombo())
        {
            Bundler.waitersComboChooserActivity(tableId, item.getItemId()).start(this);
        }
        else
        {
            Bundler.waitersPortionChooserActivity(tableId, item.getItemId()).start(this);
        }
    }

    private void showUpdatedOrder(final WaiterTable table)
    {
        final WaiterOrder orderForTable = orderPresenter.getOrderForTable(tableId);
        final int preorders = orderForTable.getPreordersCount();
        final int currorder = orderForTable.getCurrentOrderCount();

        orderComments = orderForTable.getNotes() != null ? orderForTable.getNotes() : "";

        if (preorderList != null)
        {
            preorderList.setQuickOrderMode(table.isVirtualTable());
            preorderList.setData(orderForTable, true);
        }

        if (sidebar != null)
        {
            sidebar.setQuickOrderMode(table.isVirtualTable());
            sidebar.setData(orderForTable);

            if (preorders == 0)
            {
                sidebar.closeAndLock();
            }
            else
            {
                sidebar.unlock();
            }

            final MenuItem item = toolbar.getMenu().findItem(R.id.menu_cart);

            if (item != null)
            {
                item.setVisible(preorders > 0 || currorder > 0);

                if (cartBadge != null)
                {
                    cartBadge.setText("" + preorders);
                    cartBadge.setVisibility(preorders > 0 ? View.VISIBLE : View.GONE);
                }

                toolbar.invalidate();
            }
        }

        if (preorderPanel != null)
        {
            preorderPanel.setVisibility((preorders > 0 || currorder > 0) ? View.VISIBLE : View.GONE);

            if (btnClear != null)
            {
                btnClear.setVisibility(preorders > 0 ? View.VISIBLE : View.GONE);
            }

            if (btnSubmit != null)
            {
                btnSubmit.setVisibility(preorders > 0 ? View.VISIBLE : View.GONE);
            }
        }
    }

    public void onMenuSyncError(final DineflyException e)
    {
        refresher.setRefreshing(false);
        showError(e);
    }

    public void onMenuSynced()
    {
        refresher.setRefreshing(false);
        showData();
    }

    @OnClick(R.id.waitersBtnPreorderSubmit)
    @Optional
    void onSubmitPreorderClick()
    {
        showBlockingIndeterminateProgressDialog(false, null, getString(R.string.please_wait));
        orderPresenter.checkSoldOuts(tableId);
    }

    void askPaxWaiterAndFinishOrderSubmission()
    {
        if (!tablePresenter.getTable(tableId).isVirtualTable() && orderPresenter.needToSetPaxData(tableId))
        {
            new PaxDataDialog(this).show(new PaxDataDialog.Callback()
            {
                public void onPaxDataSet(final String pax, final String waiter)
                {
                    submitPreorderAndFinish(pax, waiter);
                }

            });
        }
        else
        {
            submitPreorderAndFinish(null, null);
        }
    }

    private void submitPreorderAndFinish(final String pax, final String waiter)
    {
        if (tableId == 0)
        {
            showBlockingIndeterminateProgressDialog(false, null, getString(R.string.please_wait));
            orderPresenter.submitPreorderWithResult(tableId, pax, waiter, orderComments);
        }
        else
        {
            orderPresenter.submitPreorderWithResult(tableId, pax, waiter, orderComments);
        }
    }

    @OnClick(R.id.waitersBtnPreorderClear)
    @Optional
    void onClearPreoderClick()
    {
        orderPresenter.clearPreorder(tableId);

        if (sidebar != null)
        {
            sidebar.close();
        }
    }

    public void onOrdersDataChanged()
    {
        showUpdatedOrder(tablePresenter.getTable(tableId));
    }

    public void onTableOrdersSyncFailed(final Throwable t)
    {
    }

    public void onTableOrdersSyncCompleted()
    {
    }

    public void onBillPrintingError(final Throwable t)
    {

    }

    public void onBillPrinted()
    {

    }

    public void onPreorderSubmitFailed(final Throwable t)
    {
        hideBlockingProgressDialog();
        showError(t);
    }

    public void onPreorderSubmitted(final long serverTicketId)
    {
        orderComments = null;
        hideBlockingProgressDialog();

        if (tableId != 0)
        {
            setResult(RESULT_OK, new Intent());
            finish();
        }
        else
        {
            onClearPreoderClick();
            Bundler.waitersSuccessOrderMessageActivity(serverTicketId).start(this);
        }
    }

    @Override
    public void onSoldOutCheckResult(String message)
    {
        hideBlockingProgressDialog();

        if (TextUtils.isEmpty(message))
        {
            askPaxWaiterAndFinishOrderSubmission();
        }
        else
        {
            showMessage(message);
        }
    }

    public void onTableMovementError(final Throwable t)
    {
        // unused here
    }

    public void onTableMoved(final long ticketId, final long tableId)
    {
        // unused here
    }
}
