package com.kt.yapp.service;

import com.kt.yapp.domain.Item;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class MissionService {
    private List<Item> items;

    public MissionService() {
        items = new ArrayList<>();
        items.add(new Item("Item 1"));
        items.add(new Item("Item 2"));
        items.add(new Item("Item 3"));
        // Add more items as needed
    }

    public Item drawItem() {
        Random random = new Random();
        int index = random.nextInt(items.size());
        return items.get(index);
    }
}
