package com.dineplan.dinefly.component.commons;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import com.dineplan.dinefly.R;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/11/2017
 */
public class PinpadView extends FrameLayout
{

    Callback callback;

    public PinpadView(final Context context)
    {
        super(context);
        configure();
    }

    public PinpadView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public PinpadView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public PinpadView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    public Callback getCallback()
    {
        return callback;
    }

    public void setCallback(final Callback callback)
    {
        this.callback = callback;
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.commons_view_pinapd, this, true);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.numpad_btn_1, R.id.numpad_btn_2, R.id.numpad_btn_3, R.id.numpad_btn_4, R.id.numpad_btn_5, R.id.numpad_btn_6, R.id.numpad_btn_7, R.id.numpad_btn_8, R.id.numpad_btn_9, R.id.numpad_btn_0})
    void onNumberClick(View view)
    {
        if (view.getTag() != null)
        {
            final String key = "" + view.getTag();

            if (callback != null)
            {
                callback.onNumpadKeyPressed(key.charAt(0));
            }
        }
    }

    @OnClick(R.id.numpad_btn_backspace)
    void onBackspaceClick()
    {
        if (callback != null)
        {
            callback.onBackspaceKeyPressed();
        }
    }

    @OnClick(R.id.numpad_btn_ok)
    void onCompleteInputClick()
    {
        if (callback != null)
        {
            callback.onInputCompletedPressed();
        }
    }

    @OnLongClick(R.id.numpad_btn_backspace)
    boolean onBackspaceLongClick()
    {
        if (callback != null)
        {
            callback.onClearInputPressed();
        }

        return true;
    }

    public interface Callback
    {

        void onNumpadKeyPressed(Character key);

        void onBackspaceKeyPressed();

        void onClearInputPressed();

        void onInputCompletedPressed();
    }

}