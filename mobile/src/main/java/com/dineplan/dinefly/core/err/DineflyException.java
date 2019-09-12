package com.dineplan.dinefly.core.err;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineflyException extends RuntimeException
{

    public DineflyException(final Throwable err)
    {
        super(err);
    }

    public DineflyException(final String message)
    {
        super(message);
    }
}
