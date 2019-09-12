package com.dineplan.dinefly.data.model;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

import java.util.Date;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 06/10/2017
 */

@Entity
public class TerminalConfiguration
{

    public final static int CONFIGURATION_DEFAULT = 1;

    @Id
    long pk;

    int configurationId;

    @Convert(converter = TerminalTypeConverter.class, dbType = Integer.class)
    TerminalType terminalType;

    Date dateProvisioned;

    private transient Object transferPayload;

    public void setTransferPayload(final Object payload)
    {
        transferPayload = payload;
    }

    public Object getTransferPayload()
    {
        return transferPayload;
    }


    public static class TerminalTypeConverter implements PropertyConverter<TerminalType, Integer>
    {

        @Override
        public TerminalType convertToEntityProperty(Integer databaseValue)
        {
            if (databaseValue == null)
            {
                return null;
            }

            for (TerminalType termType : TerminalType.values())
            {
                if (termType.type == databaseValue)
                {
                    return termType;
                }
            }

            return TerminalType.Waiter;
        }

        @Override
        public Integer convertToDatabaseValue(TerminalType entityProperty)
        {
            return entityProperty == null ? null : entityProperty.type;
        }
    }

    public TerminalType getTerminalType()
    {
        return terminalType;
    }

    public void setTerminalType(final TerminalType terminalType)
    {
        this.terminalType = terminalType;
    }

    public Date getDateProvisioned()
    {
        return dateProvisioned;
    }

    public void setDateProvisioned(final Date dateProvisioned)
    {
        this.dateProvisioned = dateProvisioned;
    }

    public int getConfigurationId()
    {
        return configurationId;
    }

    public void setConfigurationId(final int configurationId)
    {
        this.configurationId = configurationId;
    }
}
