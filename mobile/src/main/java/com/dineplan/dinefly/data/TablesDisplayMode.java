package com.dineplan.dinefly.data;

import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 18/10/2017
 */
public enum TablesDisplayMode
{
    GridSimple, GridRich;

    @Override
    public String toString()
    {
        switch (this)
        {
            case GridRich:
                return App.getContext().getString(R.string.tables_mode_rich);

            case GridSimple:
                return App.getContext().getString(R.string.tables_mode_simple);

            default:
                return name();
        }
    }

    public static List<TablesDisplayMode> asList()
    {
        return Arrays.asList(values());
    }
}
