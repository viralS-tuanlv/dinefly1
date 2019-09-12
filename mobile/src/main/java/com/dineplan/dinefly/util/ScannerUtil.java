package com.dineplan.dinefly.util;

import android.app.Activity;
import android.content.Intent;
import com.dineplan.dinefly.core.App;
import in.workarounds.bundler.Bundler;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public class ScannerUtil
{

    public final static String SCANNED_DATA_EXTRA = "data";

    public static void scanWithResult(Activity initiator, int requestCode)
    {
        if (initiator != null)
        {
            initiator.startActivityForResult(Bundler.scannerActivity(true).intent(initiator), requestCode);
        }
    }

    public static void scan()
    {
        App.getInstance().startActivity(Bundler.scannerActivity(false).intent(App.getContext()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    public static String getScannedCodeFromIntent(final Intent data)
    {
        return data.hasExtra(SCANNED_DATA_EXTRA) ? data.getStringExtra(SCANNED_DATA_EXTRA) : null;
    }
}
