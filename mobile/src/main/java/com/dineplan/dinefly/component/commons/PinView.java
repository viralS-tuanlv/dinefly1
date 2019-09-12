package com.dineplan.dinefly.component.commons;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/11/2017
 */
public class PinView extends FrameLayout
{

    @BindView(R.id.pinsRoot)
    LinearLayout pinsRoot;

    StringBuffer pin = new StringBuffer(20);
    Callback callback;

    public PinView(final Context context)
    {
        super(context);
        configure();
    }

    public PinView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public PinView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    public PinView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
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

    public void addDigit(final String symbol)
    {
        if (!TextUtils.isEmpty(symbol))
        {
            pin.append(symbol.charAt(0));

            AppCompatImageView img = new AppCompatImageView(getContext());
            img.setImageResource(R.drawable.ic_pinview_on);
            pinsRoot.addView(img);
        }
    }

    public void deleteDigit()
    {
        if (pin.length() > 0)
        {
            pin.deleteCharAt(pin.length() - 1);
            pinsRoot.removeViewAt(pinsRoot.getChildCount() - 1);
        }
    }

    public void clearPin()
    {
        pin = new StringBuffer("");
        pinsRoot.removeAllViews();
    }

    public void setPinpad(PinpadView numpadView)
    {
        numpadView.setCallback(new PinpadView.Callback()
        {
            public void onNumpadKeyPressed(final Character key)
            {
                addDigit("" + key);
            }

            public void onBackspaceKeyPressed()
            {
                deleteDigit();
            }

            public void onClearInputPressed()
            {
                clearPin();
            }

            public void onInputCompletedPressed()
            {
                if (callback != null && pin.length() > 0)
                {
                    callback.onInputCompleted(pin.toString());
                }
            }
        });
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.commons_view_pinview, this, true);
        ButterKnife.bind(this);
    }

    public interface Callback
    {

        void onInputCompleted(String pin);
    }
}
