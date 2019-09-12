package com.dineplan.dinefly.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import com.arellomobile.mvp.MvpFacade;
import com.crashlytics.android.Crashlytics;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.activity.base.BaseActivity;
import com.dineplan.dinefly.core.base.BaseApp;
import com.dineplan.dinefly.util.AppSettings;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends BaseApp implements Application.ActivityLifecycleCallbacks
{

    AppSettings settings;
    DataManager dataManager;
    Activity lastRegisteredActivity;
    int activitiesUp;

    public static AppSettings getSettings()
    {
        return ((App) getInstance()).settings;
    }

    public static BaseActivity getActiveActivity()
    {
        return (BaseActivity) ((App) getInstance()).lastRegisteredActivity;
    }

    public static App getContext() {
        return (App)getInstance();
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        initFonts();
        MvpFacade.init();
        dataManager = new DataManager(this);

        settings = new AppSettings(this);
        registerActivityLifecycleCallbacks(this);
    }

    private void initFonts()
    {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                                              .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                                              .setFontAttrId(R.attr.fontPath)
                                              .build()
                                     );
    }

    public static DataManager getDataManager()
    {
        return ((App) getInstance()).dataManager;
    }

    public static boolean isTablet()
    {
        return getContext().getResources().getBoolean(R.bool.device_type_tablet);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState)
    {

    }

    @Override
    public void onActivityStarted(Activity activity)
    {

    }

    @Override
    public void onActivityResumed(Activity activity)
    {
        activitiesUp++;
        lastRegisteredActivity = activity;
    }

    @Override
    public void onActivityPaused(Activity activity)
    {
        activitiesUp--;
        lastRegisteredActivity = null;
    }

    @Override
    public void onActivityStopped(Activity activity)
    {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState)
    {

    }

    @Override
    public void onActivityDestroyed(Activity activity)
    {

    }
}
