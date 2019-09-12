package com.dineplan.dinefly.util;

import android.support.annotation.NonNull;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerDialog;
import com.sbgapps.simplenumberpicker.decimal.DecimalPickerHandler;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 12/10/2017
 */
public class DialogsUtil
{

    public static void enterAmount(@NonNull final BaseActivity host, final AmountCallback callback)
    {
        new DecimalPickerDialog.Builder().natural().callback(new DecimalPickerHandler()
        {
            public void onDecimalNumberPicked(final int reference, final float number)
            {
                if (callback != null)
                {
                    callback.onAmountProvided(number);
                }
            }
        }).show(host.getFragmentManager(), "flyAmountPicker");
    }

    public interface AmountCallback
    {

        void onAmountProvided(float amount);
    }

}
