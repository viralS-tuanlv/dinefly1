package com.dineplan.dinefly.component;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class CrossfadeWrapepr implements ICrossfader
{

    private Crossfader mCrossfader;

    public CrossfadeWrapepr(Crossfader crossfader)
    {
        this.mCrossfader = crossfader;
    }

    @Override
    public void crossfade()
    {
        mCrossfader.crossFade();
    }

    @Override
    public boolean isCrossfaded()
    {
        return mCrossfader.isCrossFaded();
    }
}