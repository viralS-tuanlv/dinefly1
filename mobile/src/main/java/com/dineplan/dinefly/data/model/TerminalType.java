package com.dineplan.dinefly.data.model;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public enum TerminalType
{
    Waiter(0), TableSelfService(1), Chef(2);

    final int type;

    TerminalType(int type)
    {
        this.type = type;
    }
}
