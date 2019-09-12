package com.dineplan.dinefly.event;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public class BarcodeScannedEvent
{

    private final String data;

    public BarcodeScannedEvent(final String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }
}
