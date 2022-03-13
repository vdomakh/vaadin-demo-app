package com.vdomakh.vaadindemoapp.dao;

import com.vdomakh.vaadindemoapp.domain.Item;

import java.util.List;

public interface ItemDAO {
    public List<Item> getAllItems();
    public List<Item> getItemsByFilter(String filter);
    public void updateItems(List<Item> newList);
}
