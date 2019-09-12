package com.dineplan.dinefly.data;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;

import java.util.Arrays;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 18/10/2017
 */
public enum MenuDisplayMode
{
    GridRich, PlainText;

    public static List<MenuDisplayMode> asList()
    {
        return Arrays.asList(values());
    }

    @Override
    public String toString()
    {
        switch (this)
        {
            case PlainText:
                return App.getContext().getString(R.string.menu_mode_table);

            case GridRich:
                return App.getContext().getString(R.string.menu_mode_grid);

            default:
                return name();
        }
    }

}
