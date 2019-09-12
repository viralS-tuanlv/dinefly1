package com.dineplan.dinefly.activity.waiters;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.crashlytics.android.Crashlytics;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.MainActivity;
import com.dineplan.dinefly.component.commons.PinView;
import com.dineplan.dinefly.component.commons.PinpadView;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.logic.waiters.WaiterLoginPresenter;
import com.rengwuxian.materialedittext.MaterialEditText;
import in.workarounds.bundler.annotations.RequireBundler;
import io.fabric.sdk.android.services.common.Crash;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
public class WaitersLoginActivity extends WaitersBaseActivity implements WaiterLoginPresenter.View
{

    @BindView(R.id.pinview)
    PinView pinTextView;

    @BindView(R.id.pinpad)
    PinpadView pinpad;

    @BindView(R.id.loginPinFieldText)
    MaterialEditText pinField;

    @InjectPresenter
    WaiterLoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (checkProvision())
        {
            setContentView(R.layout.activity_waiters_login);
            configure();
        }
    }

    private void configure()
    {
        pinField.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            public boolean onEditorAction(final TextView textView, final int i, final KeyEvent keyEvent)
            {
                if (pinField.getText().length() > 0)
                {
                    onPasscodeComplete(pinField.getText().toString());
                    return true;
                } else
                {
                    return false;
                }
            }
        });

        pinTextView.setPinpad(pinpad);
        pinTextView.setCallback(new PinView.Callback()
        {
            public void onInputCompleted(final String pin)
            {
                onPasscodeComplete(pin);
            }
        });
    }

    public void onLoginSuccessfull()
    {
        hideBlockingProgressDialog();
        quitToMainAcreen();
    }

    public void onLoginError(final Throwable err)
    {
        Crashlytics.logException(err);
        showError(err);
        hideBlockingProgressDialog();
        pinTextView.clearPin();
    }

    public void onSalesClosedLoginError()
    {
        showMessage(R.string.waiter_sales_closed);
    }

    public void onPasscodeComplete(final String s)
    {
        showBlockingIndeterminateProgressDialog(false, null, getString(R.string.loggin_in));
        loginPresenter.login(s);
    }

    @OnClick(R.id.btnUnlink)
    void onUnlinkClick()
    {
        showQuestion(R.string.device_unlink_confirmatio, new QuestionDialogCallback()
        {
            public void onPositiveAnsfer()
            {
                loginPresenter.unlink();
                startActivity(new Intent(App.getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }

            public void onNegativeAnswer()
            {

            }
        });
    }

    public void onHelpRequest()
    {
    }
}
