package com.dineplan.dinefly.core.err;

public class SoldOutError extends RuntimeException
{
    public SoldOutError(String text)
    {
        super(text);
    }
}
