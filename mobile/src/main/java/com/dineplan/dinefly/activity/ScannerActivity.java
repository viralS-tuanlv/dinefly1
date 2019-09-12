package com.dineplan.dinefly.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.event.BarcodeScannedEvent;
import com.dineplan.dinefly.util.ScannerUtil;
import com.google.zxing.Result;
import com.mikepenz.materialize.MaterializeBuilder;
import in.workarounds.bundler.Bundler;
import in.workarounds.bundler.annotations.Arg;
import in.workarounds.bundler.annotations.RequireBundler;
import in.workarounds.bundler.annotations.Required;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
@RuntimePermissions
public class ScannerActivity extends BaseActivity implements ZXingScannerView.ResultHandler
{

    @Arg
    @Required
    boolean localScan;

    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        Bundler.inject(this);
        new MaterializeBuilder(this).withFullscreen(true).withTransparentStatusBar(true).build();
    }

    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults)
    {
        ScannerActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        scannerView.setResultHandler(this);
        ScannerActivityPermissionsDispatcher.startScannerWithCheck(this);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        scannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result)
    {
        if (result!=null && !TextUtils.isEmpty(result.getText()))
        {
            if (localScan)
            {
                setResult(RESULT_OK, new Intent().putExtra(ScannerUtil.SCANNED_DATA_EXTRA, result.getText()));
                finish();
            } else
            {
                App.postEvent(new BarcodeScannedEvent(result.getText()));
                finish();
            }
        } else
        {
            scannerView.resumeCameraPreview(this);
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void startScanner()
    {
        scannerView.startCamera();
    }

}
