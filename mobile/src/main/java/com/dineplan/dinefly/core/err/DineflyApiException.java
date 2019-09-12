package com.dineplan.dinefly.core.err;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class DineflyApiException extends DineflyException
{

    private final String apiMessage;

    public DineflyApiException(final String message)
    {
        super(String.format("API error %s", message));
        this.apiMessage = message;
    }

    public String getApiMessage()
    {
        return apiMessage;
    }
}
