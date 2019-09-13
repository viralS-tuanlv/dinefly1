package com.dineplan.dinefly.activity.waiters.dialogprint;

import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.api.model.api.bill.BillData;

import java.util.List;

import eu.livotov.labs.android.robotools.os.RTAsyncTask;

public class DialogPrintPresenter {
    private View v;
    public DialogPrintPresenter(View v){
        this.v = v;
    }

    public void getBill(final long ticketId){
        v.onLoading();
        new RTAsyncTask()
        {
            BillData data = null;

            protected void doInBackground() {
                data = App.getDataManager().getWaiterDataManage().getBillFromTicket(ticketId);
            }

            protected void onPostExecute()
            {
                if (data.getBillResponse().getContent() != null) {
                    v.loadBillSuccess(data.getBillResponse().getContent());
                } else {
                    v.onError("Can not get bill from server!");
                }

            }

            protected void onError(final Throwable t)
            {
                v.onError(t.getLocalizedMessage());
            }
        }.execPool();
    }
    public interface View
    {
        void onLoading();
        void onError(String msg);
        void loadBillSuccess(List<String> content);
    }
}
