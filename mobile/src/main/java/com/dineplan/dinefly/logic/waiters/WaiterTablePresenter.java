package com.dineplan.dinefly.logic.waiters;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.data.model.waiters.WaiterRoom;
import com.dineplan.dinefly.data.model.waiters.WaiterTable;
import eu.livotov.labs.android.robotools.os.RTAsyncTask;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@InjectViewState
public class WaiterTablePresenter extends MvpPresenter<WaiterTablePresenter.View>
{

    public WaiterTable getTable(final long id)
    {
        return App.getDataManager().getWaiterDataManage().findTable(id);
    }

    public List<WaiterTable> getTablesToMoveTo(final long tableId)
    {
        List<WaiterTable> tables = new ArrayList<>();
        List<WaiterRoom> rooms = App.getDataManager().getWaiterDataManage().getHalls();

        for (WaiterRoom room : rooms)
        {
            for (WaiterTable table : room.getTables())
            {
                if (!table.isBusy() && table.getTableId()!=tableId)
                {
                    tables.add(table);
                }
            }
        }

        return tables;
    }

    public void moveToTable(final long ticketId, final long tableId)
    {
        new RTAsyncTask()
        {

            protected void doInBackground() throws Throwable
            {
                App.getDataManager().getWaiterDataManage().moveTable(ticketId, tableId);
            }

            protected void onPostExecute()
            {
                getViewState().onTableMoved(ticketId, tableId);
            }

            protected void onError(final Throwable t)
            {
                getViewState().onTableMovementError(t);
            }
        }.execPool();
    }

    @StateStrategyType(SkipStrategy.class)
    public interface View extends MvpView
    {

        void onTableMovementError(Throwable t);

        void onTableMoved(long ticketId, long tableId);
    }
}
