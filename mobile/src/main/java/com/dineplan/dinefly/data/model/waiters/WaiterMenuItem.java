package com.dineplan.dinefly.data.model.waiters;

import android.text.TextUtils;
import android.util.Log;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuCategory;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuItem;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuPortion;
import com.dineplan.dinefly.core.api.model.api.in.DineplanTagGroup;
import com.google.gson.annotations.SerializedName;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@Entity
public class WaiterMenuItem
{
    private static final Pattern FILE_LASTFOLDER_MATCHER = Pattern.compile(".*(\\\\.*)");
    public static final int ITEM_TYPE_COMBO = 1;

    @Id
    long pk;

    int itemId;

    int categoryId;

    String name;

    String alias;

    String dineplanPictrue;

    String description;

    String subTag;

    boolean favorite;

    int weight;

    int itemType;

    ToMany<WaiterMenuItemPortion> portions;

    ToMany<WaiterMenuItemTagGroup> tagGroups;

    public WaiterMenuItem()
    {
    }

    public static WaiterMenuItem createSubtagRootStub(String subtag)
    {
        WaiterMenuItem i = new WaiterMenuItem();
        i.name = subtag;
        i.pk = -1;
        i.itemId = subtag.hashCode();
        return i;
    }

    public int getItemId()
    {
        return itemId;
    }

    public void setItemId(final int itemId)
    {
        this.itemId = itemId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getAlias()
    {
        return alias;
    }

    public void setAlias(final String alias)
    {
        this.alias = alias;
    }

    public String getDineplanPictrue()
    {
        return dineplanPictrue;
    }

    public void setDineplanPictrue(final String dineplanPictrue)
    {
        this.dineplanPictrue = dineplanPictrue;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public boolean isFavorite()
    {
        return favorite;
    }

    public void setFavorite(final boolean favorite)
    {
        this.favorite = favorite;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(final int weight)
    {
        this.weight = weight;
    }

    public ToMany<WaiterMenuItemPortion> getPortions()
    {
        return portions;
    }

    public void setPortions(final ToMany<WaiterMenuItemPortion> portions)
    {
        this.portions = portions;
    }

    public ToMany<WaiterMenuItemTagGroup> getTagGroups()
    {
        return tagGroups;
    }

    public void setTagGroups(final ToMany<WaiterMenuItemTagGroup> tagGroups)
    {
        this.tagGroups = tagGroups;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(final int categoryId)
    {
        this.categoryId = categoryId;
    }

    public int getItemType()
    {
        return itemType;
    }

    public void setItemType(final int itemType)
    {
        this.itemType = itemType;
    }

    public String getSubTag()
    {
        return subTag;
    }

    public void setSubTag(String subTag)
    {
        this.subTag = subTag;
    }

    public static WaiterMenuItem createFromDineplanMennuItem(final DineplanMenuCategory category, final DineplanMenuItem dineplanMenuItem, final String dineplanApiEnpoint)
    {
        WaiterMenuItem item = new WaiterMenuItem();

        item.setItemId(dineplanMenuItem.getId());
        item.setAlias(dineplanMenuItem.getAlias());
        item.setName(dineplanMenuItem.getName());
        item.setDescription(dineplanMenuItem.getDescription());
        item.setFavorite(dineplanMenuItem.isFavorite());
        item.setWeight(dineplanMenuItem.getWeight());
        item.setCategoryId(category!=null ? category.getId(): 0);
        item.setItemType(dineplanMenuItem.getItemType());
        item.setSubTag(dineplanMenuItem.getSubTag());

        for (DineplanMenuPortion dineplanMenuPortion : dineplanMenuItem.getPortions())
        {
            item.portions.add(WaiterMenuItemPortion.createFromDineplanPortion(dineplanMenuPortion));
        }

        for (DineplanTagGroup dineplanTagGroup : dineplanMenuItem.getTagGroups())
        {
            item.tagGroups.add(WaiterMenuItemTagGroup.createFromDineplanTagGroup(dineplanTagGroup));
        }

        try
        {
            if (!TextUtils.isEmpty(dineplanMenuItem.getPictrue()))
            {
                final Matcher matcher = FILE_LASTFOLDER_MATCHER.matcher(dineplanMenuItem.getPictrue());

                if (matcher.matches())
                {
                    item.setDineplanPictrue(String.format("%s%s/download/menu%s", (dineplanApiEnpoint.toLowerCase().startsWith("http") ? "" : "http://"), dineplanApiEnpoint, matcher.group(1)).replaceAll("\\\\", "/"));
                } else
                {
                    item.setDineplanPictrue(null);
                }
            } else
            {
                item.setDineplanPictrue(null);
            }
        } catch (Throwable err)
        {
            item.setDineplanPictrue(null);
            Log.e(WaiterMenuItem.class.getSimpleName(), err.getMessage(), err);
        }

        return item;
    }

    public float findDefaultPrice()
    {
        if (portions.size()>0)
        {
            return portions.get(0).getPrice();
        } else
        {
            return 0;
        }
    }

    public WaiterMenuItemPortion findPortion(final long portionId)
    {
        for (WaiterMenuItemPortion portion : portions)
        {
            if (portionId == portion.getPortionId())
            {
                return portion;
            }
        }

        return null;
    }

    public boolean checkIfCombo()
    {
        return itemType == ITEM_TYPE_COMBO;
    }

    public boolean isSubTagRoot()
    {
        return pk==-1;
    }
}
