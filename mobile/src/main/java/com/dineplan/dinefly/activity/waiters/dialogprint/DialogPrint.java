package com.dineplan.dinefly.activity.waiters.dialogprint;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.dineplan.dinefly.R;

import java.util.ArrayList;
import java.util.List;

public class DialogPrint extends Dialog implements DialogPrintPresenter.View {

    private ImageView btnClose;
    private RecyclerView list;
    private ProgressBar loading;
    private AdapterPrint adapterPrint;

    DialogPrintPresenter dialogPrintPresenter;
    public DialogPrint(Context context, long ticketId) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.print_layout);

        setCancelable(true);
        initView(context, ticketId);
    }

    private void initView(Context context, long ticketId) {
        btnClose = findViewById(R.id.btnClose);
        list = findViewById(R.id.list);
        loading = findViewById(R.id.loading);
        dialogPrintPresenter = new DialogPrintPresenter(this);
        list.setLayoutManager(new LinearLayoutManager(context));
        dialogPrintPresenter.getBill(ticketId);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onLoading() {
        btnClose.setVisibility(View.GONE);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onError(String msg) {
        btnClose.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    @Override
    public void loadBillSuccess(List<String> content) {
        btnClose.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        List<String> newContent = new ArrayList<>();
        for (int i = 0; i < content.size(); i++) {
            if (content.get(i).length() >= 4) {
                newContent.add(content.get(i));
            }
        }
        adapterPrint = new AdapterPrint(newContent);
        list.setAdapter(adapterPrint);

//        String contents = "";
//        for (int i = 0; i < content.size(); i++) {
//            contents = contents.concat(content.get(i));
//        }
//        successText.setText(contents.replace("!", ""));

    }
}
