package com.dineplan.dinefly.core.err;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineflyNetworkException extends DineflyException
{

    private final String serverMessage;
    private final int code;

    public DineflyNetworkException(final int code, final String message)
    {
        super(String.format("Server error %s (%s)", code, message));
        this.code = code;
        this.serverMessage = message;
    }

    public String getServerMessage()
    {
        return serverMessage;
    }

    public int getCode()
    {
        return code;
    }
}
