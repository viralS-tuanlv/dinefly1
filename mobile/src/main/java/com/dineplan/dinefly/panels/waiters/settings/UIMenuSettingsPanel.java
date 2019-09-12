package com.dineplan.dinefly.panels.waiters.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.commons.SecondarySettingsItemView;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.MenuDisplayMode;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public class UIMenuSettingsPanel extends FrameLayout
{

    @BindView(R.id.settingsMenuMode)
    SecondarySettingsItemView itemMenuMode;

    @BindView(R.id.settingsMenuColsMode)
    SecondarySettingsItemView itemMenuCols;

    Callback callback;

    public UIMenuSettingsPanel(final Context context)
    {
        super(context);
        configure();
    }

    public UIMenuSettingsPanel(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public UIMenuSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public UIMenuSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    public Callback getCallback()
    {
        return callback;
    }

    public void setCallback(final Callback callback)
    {
        this.callback = callback;
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.commons_panel_settings_ui_menu, this, true);
        ButterKnife.bind(this);
        showSettings();
    }

    private void showSettings()
    {
        itemMenuMode.setDescription(App.getSettings().getMenuDisplayMode().toString());
        itemMenuCols.setDescription(App.getSettings().getMenuDisplayColumns() > 0 ? ("" + App.getSettings().getMenuDisplayColumns()) : getContext().getString(R.string.automatic_cols));
        itemMenuCols.setVisibility(App.getSettings().getMenuDisplayMode() == MenuDisplayMode.GridRich ? VISIBLE : GONE);
    }

    @OnClick(R.id.settingsMenuMode)
    public void onChangeMenuMode()
    {
        new MaterialDialog.Builder(App.getActiveActivity()).title(R.string.menu_render_mode).items(MenuDisplayMode.asList()).itemsCallback(new MaterialDialog.ListCallback()
        {
            public void onSelection(final MaterialDialog dialog, final View itemView, final int position, final CharSequence text)
            {
                App.getSettings().setMenuDisplayMode(MenuDisplayMode.asList().get(position));
                showSettings();
            }
        }).show();
    }

    @OnClick(R.id.settingsMenuColsMode)
    public void onChangeMenuColumns()
    {
        final List<String> dd = new ArrayList<>();
        final List<Integer> dv = new ArrayList<>();

        dd.add(App.getContext().getString(R.string.automatic_cols));
        dd.add(App.getContext().getString(R.string.cols_2));
        dd.add(App.getContext().getString(R.string.cols_3));
        dd.add(App.getContext().getString(R.string.cols_4));
        dd.add(App.getContext().getString(R.string.cols_5));
        dd.add(App.getContext().getString(R.string.cols_6));

        dv.add(0);
        dv.add(2);
        dv.add(3);
        dv.add(4);
        dv.add(5);
        dv.add(6);

        new MaterialDialog.Builder(App.getActiveActivity()).title(R.string.number_of_columns).items(dd).itemsCallback(new MaterialDialog.ListCallback()
        {
            public void onSelection(final MaterialDialog dialog, final View itemView, final int position, final CharSequence text)
            {
                App.getSettings().setMenuDisplayColumns(dv.get(position));
                showSettings();
            }
        }).show();
    }


    public interface Callback
    {

    }
}
