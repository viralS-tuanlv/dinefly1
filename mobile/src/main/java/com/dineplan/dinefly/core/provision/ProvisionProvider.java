package com.dineplan.dinefly.core.provision;

import android.support.annotation.NonNull;
import com.dineplan.dinefly.data.model.TerminalConfiguration;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 07/10/2017
 */
public interface ProvisionProvider
{
    boolean checkCanHandle(final String data);

    @NonNull
    TerminalConfiguration handle(final String data);

    String getName();

    String getDescription();
}
