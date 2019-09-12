package com.dineplan.dinefly.dialog.waiters;

import android.content.DialogInterface;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.component.commons.PinView;
import com.dineplan.dinefly.component.commons.PinpadView;
import com.dineplan.dinefly.core.App;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 24/10/2017
 */
public class WaitersAccessConfirmationDialog implements DialogInterface.OnCancelListener, PinView.Callback
{

    MaterialDialog dialog;

    @BindView(R.id.pinview)
    PinView pinTextView;

    @BindView(R.id.pinpad)
    PinpadView pinpad;

    Callback callback;

    public WaitersAccessConfirmationDialog(BaseActivity context)
    {
        configure(context);
    }

    private void configure(final BaseActivity context)
    {
        dialog = new MaterialDialog.Builder(context).customView(R.layout.waiters_dialog_accesspinpad, false).autoDismiss(false).canceledOnTouchOutside(false).cancelListener(this).build();
        ButterKnife.bind(this, dialog.getCustomView());

        pinTextView.setPinpad(pinpad);
        pinTextView.setCallback(this);
    }

    public WaitersAccessConfirmationDialog show(Callback callback)
    {
        this.callback = callback;
        dialog.show();
        return this;
    }

    public void onCancel(final DialogInterface dialogInterface)
    {
        failAuth();
    }

    private void confirmAccess()
    {
        if (callback != null)
        {
            callback.onAuthConfirmed();
        }

        dialog.dismiss();
        dialog = null;
    }

    private void failAuth()
    {
        if (callback != null)
        {
            callback.onAuthFailed();
        }

        dialog.dismiss();
        dialog = null;
    }

    public void onInputCompleted(final String pin)
    {
        if (App.getDataManager().getWaiterDataManage().verifyAccess(pin))
        {
            confirmAccess();
        } else
        {
            pinTextView.clearPin();
        }
    }

    public interface Callback
    {

        void onAuthConfirmed();

        void onAuthFailed();
    }
}
