package com.dineplan.dinefly.panels.waiters.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.commons.SecondarySettingsItemView;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.TablesDisplayMode;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 23/10/2017
 */
public class UITablesSettingsPanel extends FrameLayout implements CompoundButton.OnCheckedChangeListener
{

    @BindView(R.id.settingsTablesMode)
    SecondarySettingsItemView itemTablesMode;

    @BindView(R.id.settingsTablesColumns)
    SecondarySettingsItemView itemTablesCols;

    @BindView(R.id.settingsTablesBg)
    SecondarySettingsItemView itemTablesBg;

    Callback callback;

    public UITablesSettingsPanel(final Context context)
    {
        super(context);
        configure();
    }

    public UITablesSettingsPanel(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public UITablesSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public UITablesSettingsPanel(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
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
        LayoutInflater.from(getContext()).inflate(R.layout.commons_panel_settings_ui_entities, this, true);
        ButterKnife.bind(this);
        showSettings();
    }

    private void showSettings()
    {
        itemTablesMode.setDescription(App.getSettings().getTablesDisplayMode().toString());
        itemTablesCols.setDescription(App.getSettings().getTablesDisplayColumns() > 0 ? ("" + App.getSettings().getTablesDisplayColumns()) : getContext().getString(R.string.automatic_cols));

        itemTablesBg.getSwitchView().setOnCheckedChangeListener(null);
        itemTablesBg.getSwitchView().setChecked(App.getSettings().shouldShowTablesGridBackground());
        itemTablesBg.getSwitchView().setOnCheckedChangeListener(this);
    }

    @OnClick(R.id.settingsTablesMode)
    public void onChangeTablesMode()
    {
        new MaterialDialog.Builder(App.getActiveActivity()).title(R.string.tables_render_mode).items(TablesDisplayMode.asList()).itemsCallback(new MaterialDialog.ListCallback()
        {
            public void onSelection(final MaterialDialog dialog, final View itemView, final int position, final CharSequence text)
            {
                App.getSettings().setTablesDisplayMode(TablesDisplayMode.asList().get(position));
                showSettings();
            }
        }).show();
    }

    @OnClick(R.id.settingsTablesColumns)
    public void onChangeTableColumns()
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

        new MaterialDialog.Builder(App.getActiveActivity()).title(R.string.tables_render_mode_columns).items(dd).itemsCallback(new MaterialDialog.ListCallback()
        {
            public void onSelection(final MaterialDialog dialog, final View itemView, final int position, final CharSequence text)
            {
                App.getSettings().setTableDisplayColumns(dv.get(position));
                showSettings();
            }
        }).show();
    }

    @OnClick(R.id.settingsTablesBg)
    public void onChangeTablesBg()
    {
        itemTablesBg.getSwitchView().setChecked(!itemTablesBg.getSwitchView().isChecked());
    }

    public void onCheckedChanged(final CompoundButton compoundButton, final boolean b)
    {
        App.getSettings().setShowTablesGridBackground(b);
        showSettings();
    }

    public interface Callback
    {

    }
}
