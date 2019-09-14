package com.dineplan.dinefly.activity;

import android.content.Intent;
import android.os.Bundle;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.core.App;
import in.workarounds.bundler.Bundler;

/**
 * Created by dlivotov on 07/06/2016.
 */

public class MainActivity extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);

        if (App.getDataManager().isTerminalProvisioned())
        {
            switch (App.getDataManager().getConfiguration().getTerminalType())
            {
                case Waiter:
                    startActivity(Bundler.waitersMainActivity().intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    break;

                default:
                    handleUnsupportedConfiguration();
            }
        } else
        {
            startActivity(Bundler.provisionActivity().intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }

        finish();
    }

    private void handleUnsupportedConfiguration()
    {
        showQuestion(R.string.unsupported_mode_message, new QuestionDialogCallback()
        {
            public void onPositiveAnsfer()
            {
                App.getDataManager().resetDevice();
                startActivity(new Intent(App.getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

            public void onNegativeAnswer()
            {
                finish();
            }
        });
    }
}
