package com.dineplan.dinefly.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.logic.provision.ProvisioningPresenter;
import com.dineplan.dinefly.util.ScannerUtil;

import in.workarounds.bundler.annotations.RequireBundler;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by dlivotov on 07/06/2016.
 */

@RequireBundler
@RuntimePermissions
public class ProvisionActivity extends BaseActivity implements ProvisioningPresenter.View {

    private static final int REQUEST_CODE_SCANNER = 120;

    @InjectPresenter
    ProvisioningPresenter provisioningPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_provision);
        onScanQR();
    }

    public void onRequestPermissionsResult(final int requestCode, final String[] permissions, final int[] grantResults) {
        ProvisionActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SCANNER:
                    onProcessScannedCode(ScannerUtil.getScannedCodeFromIntent(data));
                    return;

                default:
                    finish();
            }
        } else {
            finish();
        }
    }

    private void onProcessScannedCode(final String data) {
        showBlockingIndeterminateProgressDialog(false, R.string.please_wait, R.string.device_being_provisioned);
        provisioningPresenter.provision(data);
    }

    void onScanQR() {
        ProvisionActivityPermissionsDispatcher.startScannerWithCheck(this);
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void startScanner() {
        ScannerUtil.scanWithResult(this, REQUEST_CODE_SCANNER);
    }

    public void onProvisionFailed() {
        showMessage(R.string.provision_failed_title, R.string.provision_failed, new DialogCloseCallback() {
            public void onDialogClosed() {

            }
        });
    }

    public void onProvisionCompleted() {
        startActivity(new Intent(App.getContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }
}
