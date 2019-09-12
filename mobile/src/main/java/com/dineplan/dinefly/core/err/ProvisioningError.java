package com.dineplan.dinefly.core.err;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public class ProvisioningError extends RuntimeException
{

    public ProvisioningError(final String message)
    {
        super(message);
    }

    public ProvisioningError(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
