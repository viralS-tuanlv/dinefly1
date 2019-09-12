package com.dineplan.dinefly.core;

import android.support.annotation.AnyThread;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import com.dineplan.dinefly.core.err.AlreadyProvisionedException;
import com.dineplan.dinefly.core.err.ProvisioningError;
import com.dineplan.dinefly.core.provision.Provisioner;
import com.dineplan.dinefly.data.model.MyObjectBox;
import com.dineplan.dinefly.data.model.TerminalConfiguration;
import com.dineplan.dinefly.data.model.TerminalConfiguration_;
import com.dineplan.dinefly.data.model.TerminalType;
import io.objectbox.BoxStore;

import java.util.Date;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */
public class DataManager
{

    final BoxStore db;
    WaiterDataManager waiterDataManage = null;

    DataManager(App app)
    {
        db = MyObjectBox.builder().androidContext(app).name("fly-3.0.0").build();
    }

    @AnyThread
    public boolean isTerminalProvisioned()
    {
        return getConfiguration() != null;
    }

    @AnyThread
    public TerminalConfiguration getConfiguration()
    {
        return db.boxFor(TerminalConfiguration.class).query().equal(TerminalConfiguration_.configurationId, TerminalConfiguration.CONFIGURATION_DEFAULT).build().findUnique();
    }

    @WorkerThread
    @Nullable
    public synchronized TerminalType provision(final String provisionData)
    {
        if (isTerminalProvisioned())
        {
            throw new AlreadyProvisionedException();
        }

        TerminalConfiguration configuration = Provisioner.provision(provisionData);
        configuration.setConfigurationId(TerminalConfiguration.CONFIGURATION_DEFAULT);
        configuration.setDateProvisioned(new Date());

        switch (configuration.getTerminalType())
        {
            case Waiter:
                WaiterDataManager.provision(db, configuration.getTransferPayload());
                break;

            default:
                throw new ProvisioningError("Unsupported terminal type: " + configuration.getTerminalType());
        }

        db.boxFor(TerminalConfiguration.class).put(configuration);

        shutdownOldManagers();

        return configuration.getTerminalType();
    }

    private void shutdownOldManagers()
    {
        if (waiterDataManage != null)
        {
            waiterDataManage.shutdown();
            waiterDataManage = null;
        }
    }

    @AnyThread
    public synchronized void resetDevice()
    {
        if (isTerminalProvisioned())
        {
            shutdownOldManagers();
            db.boxFor(TerminalConfiguration.class).removeAll();
        }
    }

    public synchronized WaiterDataManager getWaiterDataManage()
    {
        if (waiterDataManage == null)
        {
            final TerminalConfiguration configuration = getConfiguration();

            if (configuration != null && configuration.getTerminalType() == TerminalType.Waiter)
            {
                waiterDataManage = new WaiterDataManager(this);
                return waiterDataManage;
            } else
            {
                throw new ProvisioningError("No provision for waiter app");
            }
        }

        return waiterDataManage;
    }

    public boolean isTerminalProvisioned(final TerminalType terminalType)
    {
        final TerminalConfiguration configuration = getConfiguration();
        return configuration != null && configuration.getTerminalType() == terminalType;
    }
}
