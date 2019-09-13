package com.dineplan.dinefly.activity.waiters;

import android.content.Intent;
import com.dineplan.dinefly.activity.MainActivity;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.TerminalType;
import in.workarounds.bundler.Bundler;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class WaitersBaseActivity extends BaseActivity
{

    protected boolean checkProvision()
    {
        if (!App.getDataManager().isTerminalProvisioned(TerminalType.Waiter))
        {
            App.getContext().startActivity(new Intent(App.getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            return false;
        } else
        {
            return true;
        }
    }

    protected boolean checkAuthorisation()
    {
        if (!App.getDataManager().isTerminalProvisioned(TerminalType.Waiter))
        {
            App.getContext().startActivity(new Intent(App.getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            return false;
        }

        if (!App.getDataManager().getWaiterDataManage().isAuthenticated())
        {
            App.getContext().startActivity(new Intent(App.getContext(), WaitersLoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
            return false;
        } else
        {
            return true;
        }
    }

    protected void quitToMainAcreen()
    {
        finishAllInstances();
        App.getContext().startActivity(Bundler.waitersMainActivity().intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
