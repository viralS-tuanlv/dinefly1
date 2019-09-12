package com.dineplan.dinefly.activity.waiters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.component.waiters.MenuGrid;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import com.dineplan.dinefly.logic.waiters.WaiterScratchpadPresenter;

import java.util.List;

import butterknife.BindView;

/**
 * Created by dlivotov on 07/06/2016.
 */

public class WaitersSubtagSelectionActivity extends WaitersBaseActivity implements WaiterScratchpadPresenter.View
{

    @InjectPresenter(type = PresenterType.GLOBAL, tag = "fly")
    WaiterScratchpadPresenter scratchpadPresenter;

    @BindView(R.id.waitersToolbar)
    Toolbar toolbar;

    @BindView(R.id.subtagsList)
    MenuGrid subtagsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setResult(Activity.RESULT_CANCELED);

        if (checkProvision())
        {
            setContentView(R.layout.activity_waiters_subtags);
            configure();
        } else
        {
            finish();
        }
    }

    private void configure()
    {
        if (scratchpadPresenter.menuItem == null || scratchpadPresenter.menuItems == null)
        {
            finish();
            return;
        }

        subtagsList.setData(scratchpadPresenter.menuItems, true);
        subtagsList.setCallback(new MenuGrid.Callback() {
            @Override
            public void onTableSelected(WaiterMenuItem item, List<WaiterMenuItem> tagData) {
                scratchpadPresenter.selectedMenuItem = item;
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        toolbar.setTitle(scratchpadPresenter.menuItem.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
