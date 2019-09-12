package com.dineplan.dinefly.component.waiters;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.dineplan.dinefly.R;
import com.dineplan.dinefly.core.App;
import com.dineplan.dinefly.core.glide.GlideApp;
import com.dineplan.dinefly.data.model.waiters.WaiterMenuItem;
import com.dineplan.dinefly.util.FormattingUtil;

import java.util.StringTokenizer;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
public class MenuItemRowView extends FrameLayout
{

    @BindView(R.id.primary_action)
    ImageView picture;

    @BindView(R.id.secondary_action)
    TextView price;

    @BindView(R.id.first_text_view)
    TextView title;

    @BindView(R.id.second_text_view)
    TextView description;

    @BindView(R.id.primary_action_text)
    TextView alias;

    public MenuItemRowView(final Context context)
    {
        super(context);
        configure();
    }

    public MenuItemRowView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
        configure();
    }

    public MenuItemRowView(final Context context, final AttributeSet attrs, final int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        configure();
    }

    @TargetApi(21)
    public MenuItemRowView(final Context context, final AttributeSet attrs, final int defStyleAttr, final int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        configure();
    }

    public void showData(@NonNull WaiterMenuItem item)
    {
        price.setText(item.isSubTagRoot() ? "" : FormattingUtil.formatMenuPrice(item.findDefaultPrice()));
        title.setText(item.getName());

        if (App.isTablet() && !TextUtils.isEmpty(item.getDescription()))
        {
            description.setText(item.getDescription());
            description.setVisibility(VISIBLE);
        } else
        {
            description.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(item.getDineplanPictrue()))
        {
            GlideApp.with(picture.getContext()).load(item.getDineplanPictrue()).error(R.drawable.art_fallback_menuitem).into(picture);
            alias.setVisibility(INVISIBLE);
        } else
        {
            if (item.isSubTagRoot())
            {
                alias.setVisibility(INVISIBLE);
                picture.setVisibility(View.VISIBLE);
                picture.setImageResource(R.drawable.ic_collections_bookmark_black_24dp);
            } else
            {
                alias.setVisibility(VISIBLE);

                if (!TextUtils.isEmpty(item.getName()))
                {
                    final StringTokenizer tok = new StringTokenizer(item.getName(), " ", false);

                    if (tok.countTokens() == 1)
                    {
                        final String t = tok.nextToken();
                        alias.setText(t.length() > 1 ? t.substring(0, 2) : t);
                    }
                    else
                    {
                        final String t1 = tok.nextToken();
                        final String t2 = tok.nextToken();
                        alias.setText(String.format("%s%s", (t1.length() > 0 ? t1.substring(0, 1) : ""), (t2.length() > 0 ? t2.substring(0, 1) : "")));
                    }
                }
                else
                {
                    alias.setText(!TextUtils.isEmpty(item.getAlias()) ? item.getAlias() : "?");
                }
            }
        }
    }

    private void configure()
    {
        LayoutInflater.from(getContext()).inflate(R.layout.waiters_view_menuitem_row, this, true);
        ButterKnife.bind(this);
    }

}
