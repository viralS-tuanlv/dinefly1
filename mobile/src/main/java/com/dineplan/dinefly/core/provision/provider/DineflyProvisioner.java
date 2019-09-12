package com.dineplan.dinefly.core.provision.provider;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.err.ProvisioningError;
import com.dineplan.dinefly.core.provision.ProvisionProvider;
import com.dineplan.dinefly.data.model.TerminalConfiguration;
import com.dineplan.dinefly.data.model.TerminalType;
import com.dineplan.dinefly.data.model.waiters.WaiterConfiguration;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 07/10/2017
 */
public class DineflyProvisioner implements ProvisionProvider
{

    public boolean checkCanHandle(final String data)
    {
        return !TextUtils.isEmpty(data) && new StringTokenizer(data, ";", false).countTokens() == 2;
    }

    @NonNull
    public TerminalConfiguration handle(final String data)
    {
        if (checkCanHandle(data))
        {
            final StringTokenizer tokenizer = new StringTokenizer(data, ";", false);
            final String deviceId = tokenizer.nextToken();
            final String endpoint = tokenizer.nextToken();
            return buildProvision(deviceId, endpoint);
        } else
        {
            throw new ProvisioningError("Unsupported data: " + data);
        }
    }

    public String getName()
    {
        return App.getContext().getString(R.string.provisioner_dinefly_name);
    }

    public String getDescription()
    {
        return App.getContext().getString(R.string.provisioner_dinefly_desc);
    }

    @NonNull
    private TerminalConfiguration buildProvision(final String deviceId, final String endpoint)
    {
        try
        {
            TerminalConfiguration configuration = new TerminalConfiguration();
            configuration.setDateProvisioned(new Date());
            configuration.setTerminalType(TerminalType.Waiter);

            WaiterConfiguration waiterSession = new WaiterConfiguration();
            waiterSession.setDeviceId(deviceId);
            waiterSession.setDineplanEndpoint(endpoint);

            configuration.setTransferPayload(waiterSession);

            return configuration;
        } catch (Throwable err)
        {
            throw new ProvisioningError(err.getMessage(), err);
        }
    }

}
