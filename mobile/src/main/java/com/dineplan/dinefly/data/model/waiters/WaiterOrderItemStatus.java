package com.dineplan.dinefly.data.model.waiters;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 13/10/2017
 */
public enum WaiterOrderItemStatus
{
    DraftImmediate(0), DraftDelayed(4), PendingToBeServerLater(5), PendingSubmitted(6), PendingTakeFromWaitlist(7), ToBeServerLater(1), Submitted(2), Ready(3);

    int type;

    WaiterOrderItemStatus(int type)
    {
        this.type = type;
    }

    public static WaiterOrderItemStatus fromDineplanStatus(final String status)
    {
        if (!TextUtils.isEmpty(status))
        {
            if (status.toLowerCase().contains("submitted") || status.toLowerCase().contains("new"))
            {
                return Submitted;
            }

            if (status.toLowerCase().contains("servelater"))
            {
                return ToBeServerLater;
            }
        }

        return Submitted;
    }

    public String toDineplanStatus()
    {
        switch (this)
        {
            case DraftDelayed:
            case PendingToBeServerLater:
            case ToBeServerLater:
                return "ServeLater";

            case DraftImmediate:
            case PendingSubmitted:
                return "New";

            case Submitted:
                return "Submitted";

            case PendingTakeFromWaitlist:
                return "ServeNow";

            case Ready:
                return "Ready";

            default:
                return "Submitted";
        }
    }

    public List<Integer> toDineplanStatusCodes()
    {
        List<Integer> codes = new ArrayList<>();

        switch (this)
        {
            case DraftDelayed:
            case ToBeServerLater:
            case PendingToBeServerLater:
                codes.add(3);
                break;

            case PendingTakeFromWaitlist:
                codes.add(2);
                break;

            case DraftImmediate:
            case PendingSubmitted:
                codes.add(0);
                break;

            case Submitted:
                codes.add(1);
                break;
        }

        return codes;
    }
}
