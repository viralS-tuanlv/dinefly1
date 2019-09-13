package com.dineplan.dinefly.activity.waiters;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.panels.commons.settings.MainSettingsPanel;
import com.dineplan.dinefly.panels.waiters.settings.UIAppLocksSettingsPanel;
import com.dineplan.dinefly.panels.waiters.settings.UIMenuSettingsPanel;
import com.dineplan.dinefly.panels.waiters.settings.UITablesSettingsPanel;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.RequireBundler;

@RequireBundler
public class WaitersSettingsActivity extends WaitersBaseActivity implements MainSettingsPanel.Callback
{

    @BindView(R.id.mainSettingsPanel)
    MainSettingsPanel mainSettingsPanel;

    @BindView(R.id.secondarySettingsRoot)
    @Nullable
    FrameLayout secondarySettingsRoot;

    @BindView(R.id.waitersToolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (checkProvision())
        {
            setContentView(R.layout.activity_waiters_settings);
            configure();
        }
    }

    private void configure()
    {
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            public void onClick(final View view)
            {
                onBackPressed();
            }
        });

        mainSettingsPanel.setCallback(this);
        mainSettingsPanel.setStateless(secondarySettingsRoot == null);

        if (secondarySettingsRoot != null)
        {
            mainSettingsPanel.onEntitiesSettingsClick();
        }
    }


    public void onOpenEntitiesUISettings()
    {
        if (secondarySettingsRoot != null)
        {
            secondarySettingsRoot.removeAllViews();
            secondarySettingsRoot.addView(new UITablesSettingsPanel(secondarySettingsRoot.getContext()));
            toolbar.setTitle(getString(R.string.dinefly_settings_tablet, getString(R.string.visualisation)));
        } else
        {
            Bundler.waitersSettingsTablesActivity().start(this);
        }
    }

    public void onOpenMenuUISettings()
    {
        if (secondarySettingsRoot != null)
        {
            secondarySettingsRoot.removeAllViews();
            secondarySettingsRoot.addView(new UIMenuSettingsPanel(secondarySettingsRoot.getContext()));
            toolbar.setTitle(getString(R.string.dinefly_settings_tablet, getString(R.string.menu_visualisation)));
        } else
        {
            Bundler.waitersSettingsMenuActivity().start(this);
        }
    }

    public void onAppLocksUISettings()
    {
        if (secondarySettingsRoot != null)
        {
            secondarySettingsRoot.removeAllViews();
            secondarySettingsRoot.addView(new UIAppLocksSettingsPanel(secondarySettingsRoot.getContext()));
            toolbar.setTitle(getString(R.string.dinefly_settings_tablet, getString(R.string.app_locks)));
        } else
        {
            Bundler.waitersSettingsAppLocksActivity().start(this);
        }
    }
}
