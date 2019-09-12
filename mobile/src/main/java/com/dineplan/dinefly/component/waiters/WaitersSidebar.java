package com.dineplan.dinefly.component.waiters;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.component.CrossfadeWrapepr;
import com.dineplan.dinefly.core.App;
import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.materialdrawer.*;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialize.util.UIUtils;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
public class WaitersSidebar
{

    IProfile profileItem;
    AccountHeader accountHeader;
    Drawer drawer;
    IDrawerItem menuItemHome;
    IDrawerItem menuItemSettings;
    IDrawerItem menuItemLogout;

    Callback callback;
    Crossfader crossFader;
    MiniDrawer miniDrawer;

    public WaitersSidebar(final BaseActivity host, final Toolbar hostToolbar, final Callback callback)
    {
        this.callback = callback;
        configure(host, hostToolbar);
    }

    private void configure(final BaseActivity host, final Toolbar hostToolbar)
    {
        configureCommonStuff(host, hostToolbar);

        if (App.isTablet())
        {
            configureTablet(host, hostToolbar);
        } else
        {
            configurePhone(host, hostToolbar);
        }
    }

    private void configureCommonStuff(final BaseActivity host, final Toolbar hostToolbar)
    {
        profileItem = new ProfileDrawerItem().withName(App.getDataManager().getWaiterDataManage().getConfiguration().getUserName()).withIcon(R.mipmap.ic_launcher);
        accountHeader = new AccountHeaderBuilder()
                                .withActivity(host)
                                .withHeaderBackground(R.drawable.bg_header_account)
                                .withTranslucentStatusBar(false)
                                .withAlternativeProfileHeaderSwitching(false)
                                .withCurrentProfileHiddenInList(true)
                                .withOnlyMainProfileImageVisible(true)
                                .withCloseDrawerOnProfileListClick(true)
                                .withSelectionListEnabledForSingleProfile(false)
                                .withSelectionListEnabled(false)
                                .addProfiles(profileItem)
                                .build();

        menuItemHome = new PrimaryDrawerItem().withName(R.string.waiters_menu_home).withIcon(R.drawable.ic_home_black_24dp).withSelectedIcon(R.drawable.ic_home_white_24dp).withSelectable(true).withSelectedColorRes(R.color.primary).withSelectedTextColor(Color.WHITE).withSelectedIconColor(Color.WHITE).withIconColor(Color.BLACK);
        menuItemSettings = new PrimaryDrawerItem().withName(R.string.waiters_menu_settings).withIcon(R.drawable.ic_settings_black_24dp).withSelectable(false);
        menuItemLogout = new PrimaryDrawerItem().withName(R.string.waiters_menu_logout).withIcon(R.drawable.ic_exit_to_app_black_24dp).withSelectable(false);
    }

    private void configurePhone(final BaseActivity host, final Toolbar hostToolbar)
    {
        drawer = new DrawerBuilder()
                         .withActivity(host)
                         .withToolbar(hostToolbar)
                         .withTranslucentStatusBar(true)
                         .withDisplayBelowStatusBar(false)
                         .withAccountHeader(accountHeader)
                         .addDrawerItems(menuItemHome)
                         .addStickyDrawerItems(menuItemSettings, menuItemLogout)
                         .withFooterDivider(false)
                         .withStickyFooterDivider(false)
                         .withHeaderDivider(false)
                         .withStickyFooterShadow(false)
                         .withGenerateMiniDrawer(false)
                         .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
                         {
                             public boolean onItemClick(final View view, final int position, final IDrawerItem drawerItem)
                             {
                                 onMenuItemClick(drawerItem);
                                 return true;
                             }
                         })
                         .build();

        hostToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
    }

    private void configureTablet(final BaseActivity host, final Toolbar hostToolbar)
    {
        hostToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        drawer = new DrawerBuilder()
                         .withActivity(host)
                         .withToolbar(hostToolbar)
                         .withTranslucentStatusBar(false)
                         .withAccountHeader(accountHeader)
                         .addDrawerItems(menuItemHome)
                         .addStickyDrawerItems(menuItemSettings, menuItemLogout)
                         .withFooterDivider(false)
                         .withStickyFooterDivider(false)
                         .withHeaderDivider(false)
                         .withStickyFooterShadow(false)
                         .withGenerateMiniDrawer(true)
                         .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener()
                         {
                             public boolean onItemClick(final View view, final int position, final IDrawerItem drawerItem)
                             {
                                 onMenuItemClick(drawerItem);
                                 return true;
                             }
                         })
                         .buildView();

        miniDrawer = drawer.getMiniDrawer();
        miniDrawer.withEnableSelectedMiniDrawerItemBackground(true);
        miniDrawer.withEnableProfileClick(false);

        int firstWidth = (int) UIUtils.convertDpToPixel(300, host);
        int secondWidth = (int) UIUtils.convertDpToPixel(72, host);

        crossFader = new Crossfader()
                             .withContent(host.findViewById(R.id.waitersCrossfadeContent))
                             .withGmailStyleSwiping()
                             .withFirst(drawer.getSlider(), firstWidth)
                             .withSecond(miniDrawer.build(host), secondWidth)
                             .build();

        crossFader.getSecond().setBackgroundColor(host.getResources().getColor(R.color.waiters_sidebarmini_bg));
        miniDrawer.withCrossFader(new CrossfadeWrapepr(crossFader));
        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);
    }

    private void onMenuItemClick(final IDrawerItem menuItem)
    {
        if (callback != null)
        {
            if (menuItem == menuItemLogout)
            {
                drawer.closeDrawer();
                callback.onSidebarMenuLogoff();
            }

            if (menuItem == menuItemSettings)
            {
                drawer.closeDrawer();
                callback.onSidebarMenuSettings();
            }
        }
    }

    public void toggle()
    {
        if (drawer.isDrawerOpen())
        {
            drawer.closeDrawer();
        } else
        {
            if (crossFader != null)
            {
                crossFader.crossFade();
            } else
            {
                drawer.openDrawer();
            }
        }
    }

    public interface Callback
    {

        void onSidebarMenuLogoff();

        void onSidebarMenuSettings();
    }

}
