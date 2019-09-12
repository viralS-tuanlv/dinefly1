package com.dineplan.dinefly.core.err;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public class AlreadyProvisionedException extends IllegalStateException
{

    private static final String MESSAGE = "Terminal is already provisioned!";

    public AlreadyProvisionedException()
    {
        super(MESSAGE);
    }

    public AlreadyProvisionedException(final Throwable cause)
    {
        super(MESSAGE, cause);
    }
}
