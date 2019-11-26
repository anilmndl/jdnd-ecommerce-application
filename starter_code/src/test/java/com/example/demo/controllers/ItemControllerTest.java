package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getItemsByName_returns_not_found(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemsByName_returns_items(){
        // create items
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("test item1");
        item1.setPrice(BigDecimal.ZERO);
        item1.setDescription("item 1 description");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("test item2");
        item2.setPrice(BigDecimal.ZERO);
        item2.setDescription("item 2 description");

        when(itemRepository.findByName("test")).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItemsByName("test");

        assertNotNull(response);

        List<Item> items = response.getBody();

        assertEquals(2, items.size());
        assertEquals(true, item1.getName().contains("item"));
        assertEquals(true, item2.getName().contains("item"));
    }

    @Test
    public void getItems_returns_items(){
        // create items
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("test item1");
        item1.setPrice(BigDecimal.ZERO);
        item1.setDescription("item 1 description");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("test item2");
        item2.setPrice(BigDecimal.ZERO);
        item2.setDescription("item 2 description");

        when(itemRepository.findAll()).thenReturn(Arrays.asList(item1, item2));

        ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);

        List<Item> items = response.getBody();

        assertEquals(2, items.size());
    }

    @Test
    public void getItemById_returns_items(){
        // create items
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("test item1");
        item1.setPrice(BigDecimal.ZERO);
        item1.setDescription("item 1 description");

        Item item2 = new Item();
        item2.setId(1L);
        item2.setName("test item2");
        item2.setPrice(BigDecimal.ZERO);
        item2.setDescription("item 2 description");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));

        ResponseEntity<Item> response = itemController.getItemById(1L);

        assertNotNull(response);

        Item item = response.getBody();

        assertEquals(item.getName(), "test item1");
    }
}
