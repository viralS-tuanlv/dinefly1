package com.dineplan.dinefly.core.provision;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;
import com.dineplan.dinefly.core.err.ProvisioningError;
import com.dineplan.dinefly.core.provision.provider.DineflyProvisioner;
import com.dineplan.dinefly.data.model.TerminalConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public class Provisioner
{

    private static List<ProvisionProvider> providers = new ArrayList();

    static
    {
        providers.add(new DineflyProvisioner());
    }

    @WorkerThread
    @NonNull
    public static TerminalConfiguration provision(final String data)
    {
        if (TextUtils.isEmpty(data))
        {
            throw new ProvisioningError("Invalid provision data.");
        }

        for (ProvisionProvider provider : providers)
        {
            if (provider.checkCanHandle(data))
            {
                return provider.handle(data);
            }
        }

        throw new ProvisioningError("Unknown provision data.");
    }
}
