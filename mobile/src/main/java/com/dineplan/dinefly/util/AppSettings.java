package com.dineplan.dinefly.util;

import android.content.Context;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.MenuDisplayMode;
import com.dineplan.dinefly.data.TablesDisplayMode;
import com.dineplan.dinefly.event.SettingsChangedEvent;
import eu.livotov.labs.android.robotools.settings.RTPrefs;


public class AppSettings extends RTPrefs
{

    public AppSettings(Context ctx)
    {
        super(ctx, "dflyv2");
    }

    public TablesDisplayMode getTablesDisplayMode()
    {
        try
        {
            return TablesDisplayMode.valueOf(getString(R.string.prefs_displaymode_tables, TablesDisplayMode.GridSimple.name()));
        } catch (Throwable err)
        {
            return TablesDisplayMode.GridSimple;
        }
    }

    public void setTablesDisplayMode(TablesDisplayMode mode)
    {
        setString(R.string.prefs_displaymode_tables, mode != null ? mode.name() : TablesDisplayMode.GridRich.name());
        App.postEvent(new SettingsChangedEvent());
    }

    public int getTablesDisplayColumns()
    {
        return getInt(R.string.prefs_displaymode_tables_columns, 0);
    }

    public void setTableDisplayColumns(int cols)
    {
        setInt(R.string.prefs_displaymode_tables_columns, cols);
        App.postEvent(new SettingsChangedEvent());
    }

    public boolean shouldShowTablesGridBackground()
    {
        return getBoolean(R.string.prefs_displaymode_tables_background, false);
    }

    public void setShowTablesGridBackground(boolean show)
    {
        setBoolean(R.string.prefs_displaymode_tables_background, show);
        App.postEvent(new SettingsChangedEvent());
    }

    public MenuDisplayMode getMenuDisplayMode()
    {
        try
        {
            return MenuDisplayMode.valueOf(getString(R.string.prefs_displaymode_menu_mode, MenuDisplayMode.PlainText.name()));
        } catch (Throwable err)
        {
            return MenuDisplayMode.PlainText;
        }
    }

    public void setMenuDisplayMode(MenuDisplayMode mode)
    {
        setString(R.string.prefs_displaymode_menu_mode, mode != null ? mode.name() : MenuDisplayMode.GridRich.name());
        App.postEvent(new SettingsChangedEvent());
    }

    public void setMenuDisplayColumns(final int cols)
    {
        setInt(R.string.prefs_displaymode_menu_columns, cols >= 0 ? cols : 0);
        App.postEvent(new SettingsChangedEvent());
    }

    public int getMenuDisplayColumns()
    {
        return getInt(R.string.prefs_displaymode_menu_columns, 0);
    }

    public boolean isSettingsLockActive()
    {
        return getBoolean(R.string.prefs_security_lock_settings, false);
    }

    public boolean isLogoutLockActive()
    {
        return getBoolean(R.string.prefs_security_lock_logout, false);
    }

    public boolean isTableLockActive()
    {
        return getBoolean(R.string.prefs_security_lock_table, false);
    }

    public void setSettingsLockActive(boolean active)
    {
        setBoolean(R.string.prefs_security_lock_settings, active);
        App.postEvent(new SettingsChangedEvent());
    }
    public void setLogoutLockActive(boolean active)
    {
        setBoolean(R.string.prefs_security_lock_logout, active);
        App.postEvent(new SettingsChangedEvent());
    }
    public void setTableLockActive(boolean active)
    {
        setBoolean(R.string.prefs_security_lock_table, active);
        App.postEvent(new SettingsChangedEvent());
    }


}
