package com.dineplan.dinefly.dialog.waiters;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.core.App;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 24/10/2017
 */
public class PaxDataDialog implements DialogInterface.OnCancelListener, MaterialDialog.SingleButtonCallback
{

    @BindView(R.id.waiterHeader)
    TextView waiterHeader;

    @BindView(R.id.paxHeader)
    TextView paxHeader;

    @BindView(R.id.waiterGroup)
    RadioGroup waiterGroup;

    @BindView(R.id.paxGroup)
    RadioGroup paxGroup;

    MaterialDialog dialog;
    Callback callback;

    private String defaultPax;
    private String defaultWaiter;
    private List<String> pax;
    private List<String> waiters;

    public PaxDataDialog(BaseActivity context)
    {
        configure(context);
    }

    private void configure(final BaseActivity context)
    {
        dialog = new MaterialDialog.Builder(context).title(R.string.configure_ticket).customView(R.layout.waiters_dialog_paxdata, true).autoDismiss(false).canceledOnTouchOutside(false).cancelListener(this).positiveText(R.string.confirm).onPositive(this).build();
        ButterKnife.bind(this, dialog.getCustomView());
    }

    public PaxDataDialog show(Callback callback)
    {
        return show(null, null, callback);
    }

    public PaxDataDialog show(String defaultWaiter, String defaultPax, Callback callback)
    {
        this.callback = callback;
        this.defaultPax = defaultPax;
        this.defaultWaiter = defaultWaiter;

        pax = App.getDataManager().getWaiterDataManage().getConfiguration().getPaxData();
        waiters = App.getDataManager().getWaiterDataManage().getConfiguration().getWaiterData();

        if (pax.size() == 0 && waiters.size() == 0)
        {
            if (callback != null)
            {
                callback.onPaxDataSet(null, null);
            }
        }
        else
        {
            showData();
            dialog.show();
        }

        return this;
    }

    private void showData()
    {
        if (waiters.size() > 0)
        {
            waiterGroup.setVisibility(View.VISIBLE);
            waiterHeader.setVisibility(View.VISIBLE);

            for (String waiter : waiters)
            {
                AppCompatRadioButton btn = new AppCompatRadioButton(waiterGroup.getContext());
                btn.setTag(waiter);
                btn.setText(waiter);

                waiterGroup.addView(btn);

                if (waiter.equalsIgnoreCase(defaultWaiter))
                {
                    btn.setChecked(true);
                }

                RadioGroup.LayoutParams lp = (RadioGroup.LayoutParams) btn.getLayoutParams();
                lp.width = RadioGroup.LayoutParams.MATCH_PARENT;
                lp.bottomMargin = 20;
                btn.setLayoutParams(lp);
            }

            waiterGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(final RadioGroup radioGroup, final int i)
                {
                    try
                    {
                        defaultWaiter = (String) waiterGroup.findViewById(i).getTag();
                    }
                    catch (Throwable err)
                    {
                        defaultWaiter = null;
                    }
                }
            });
        }
        else
        {
            waiterGroup.setVisibility(View.GONE);
            waiterHeader.setVisibility(View.GONE);
        }

        if (pax.size() > 0)
        {
            paxGroup.setVisibility(View.VISIBLE);
            paxHeader.setVisibility(View.VISIBLE);

            for (String p : pax)
            {
                AppCompatRadioButton btn = new AppCompatRadioButton(paxGroup.getContext());
                btn.setTag(p);
                btn.setText(p);

                paxGroup.addView(btn);

                if (p.equalsIgnoreCase(defaultPax))
                {
                    btn.setChecked(true);
                }

                RadioGroup.LayoutParams lp = (RadioGroup.LayoutParams) btn.getLayoutParams();
                lp.width = RadioGroup.LayoutParams.MATCH_PARENT;
                lp.bottomMargin = 20;
                btn.setLayoutParams(lp);
            }

            paxGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                public void onCheckedChanged(final RadioGroup radioGroup, final int i)
                {
                    try
                    {
                        defaultPax = (String) paxGroup.findViewById(i).getTag();
                    }
                    catch (Throwable err)
                    {
                        defaultPax = null;
                    }
                }
            });
        }
        else
        {
            paxGroup.setVisibility(View.GONE);
            paxHeader.setVisibility(View.GONE);
        }
    }

    public void onCancel(final DialogInterface dialogInterface)
    {
        dialog.dismiss();
        dialog = null;
    }

    public void onClick(@NonNull final MaterialDialog d, @NonNull final DialogAction which)
    {
        if (callback != null)
        {
            if ((TextUtils.isEmpty(defaultPax) && pax.size() > 0) || (TextUtils.isEmpty(defaultWaiter) && waiters.size() > 0))
            {
                App.showToast(R.string.please_select_mandatory_paxwaiter, true);
            }
            else
            {
                callback.onPaxDataSet(defaultPax, defaultWaiter);
                dialog.dismiss();
                dialog = null;
            }
        }
    }

    public interface Callback
    {

        void onPaxDataSet(String pax, String waiter);
    }
}
