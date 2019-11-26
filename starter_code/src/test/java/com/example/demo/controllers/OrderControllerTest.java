package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.hibernate.criterion.Order;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);


    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void submit_creates_order_from_cart(){
        Item item = new Item();
        item.setId(1L);
        item.setName("test item");
        item.setPrice(BigDecimal.TEN);
        item.setDescription("test item desc");

        // add two items so that the size is 2
        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item);

        Cart cart = new Cart();
        cart.setItems(items);

        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setCart(cart);
        when(userRepository.findByUsername("test")).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit("test");
        assertNotNull(response);

        UserOrder userOrder = response.getBody();
        assertNotNull(userOrder);
        assertEquals(cart.getTotal(), userOrder.getTotal());
    }

    @Test
    public void getOrdersForUser_returns_user_orders(){
        Item item = new Item();
        item.setId(1L);
        item.setName("test item");
        item.setPrice(BigDecimal.TEN);
        item.setDescription("test item desc");

        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        UserOrder order = new UserOrder();
        order.setId(1L);
        order.setItems(Arrays.asList(item, item));
        order.setUser(user);
        order.setTotal(new BigDecimal(20));

        when(orderRepository.findByUser(user)).thenReturn(Arrays.asList(order));

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");

        assertNotNull(response);
        assertEquals(order.getItems().size(), 2);
    }

}
