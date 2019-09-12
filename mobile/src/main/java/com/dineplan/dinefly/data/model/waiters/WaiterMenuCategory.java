package com.dineplan.dinefly.data.model.waiters;

import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuCategory;
import com.dineplan.dinefly.core.api.model.api.in.DineplanMenuItem;
import com.dineplan.dinefly.core.api.model.api.in.DineplanTagGroup;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

import java.util.Iterator;

/**
 * (c) Livotov Labs Ltd. 2012
 * Date: 08/10/2017
 */
@Entity
public class WaiterMenuCategory
{

    @Id
    long pk;

    int categoryId;

    String name;

    int weight;

    String dineplanPicture;

    String description;

    ToMany<WaiterMenuItem> menuItems;

    ToMany<WaiterMenuItemTagGroup> tagGroups;

    boolean hidden;

    public WaiterMenuCategory()
    {
    }

    public long getPk()
    {
        return pk;
    }

    public void setPk(final long pk)
    {
        this.pk = pk;
    }

    public int getCategoryId()
    {
        return categoryId;
    }

    public void setCategoryId(final int categoryId)
    {
        this.categoryId = categoryId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight(final int weight)
    {
        this.weight = weight;
    }

    public String getDineplanPicture()
    {
        return dineplanPicture;
    }

    public void setDineplanPicture(final String dineplanPicture)
    {
        this.dineplanPicture = dineplanPicture;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(final String description)
    {
        this.description = description;
    }

    public Iterator<WaiterMenuItem> getMenuItems()
    {
        return menuItems.iterator();
    }

    public ToMany<WaiterMenuItemTagGroup> getTagGroups()
    {
        return tagGroups;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public static WaiterMenuCategory createFromDineplanCategory(final DineplanMenuCategory dineplanCategory, final String dineplanApiEndpoint)
    {
        WaiterMenuCategory category = new WaiterMenuCategory();

        category.setCategoryId(dineplanCategory.getId());
        category.setDescription(dineplanCategory.getDescription());
        category.setDineplanPicture(dineplanCategory.getPicture());
        category.setName(dineplanCategory.getName());
        category.setWeight(dineplanCategory.getWeight());

        for (DineplanMenuItem dineplanMenuItem : dineplanCategory.getItems())
        {
            category.menuItems.add(WaiterMenuItem.createFromDineplanMennuItem(dineplanCategory, dineplanMenuItem, dineplanApiEndpoint));
        }

        for (DineplanTagGroup group : dineplanCategory.getTagGroups())
        {
            category.tagGroups.add(WaiterMenuItemTagGroup.createFromDineplanTagGroup(group));
        }

        return category;
    }


    public void addMenuItem(WaiterMenuItem item) {
        menuItems.add(item);
    }
}
