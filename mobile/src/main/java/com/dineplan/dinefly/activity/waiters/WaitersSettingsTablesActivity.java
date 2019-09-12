package com.dineplan.dinefly.activity.waiters;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import butterknife.BindView;
import com.dineplan.dinefly.R;
import in.workarounds.bundler.annotations.RequireBundler;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
public class WaitersSettingsTablesActivity extends WaitersBaseActivity
{

    @BindView(R.id.waitersToolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (checkProvision())
        {
            setContentView(R.layout.activity_waiters_settings_tables);
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
    }
}
