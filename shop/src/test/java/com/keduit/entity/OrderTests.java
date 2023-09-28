package com.keduit.entity;

import com.keduit.constant.ItemSellStatus;
import com.keduit.repository.ItemRepository;
import com.keduit.repository.MemberRepository;
import com.keduit.repository.OrderItemRepository;
import com.keduit.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class OrderTests {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setItemDetail("상세설명");
        item.setPrice(50000);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(1000);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }
    public Order createOrder(){
    Order order = new Order();
        for(int i =0; i<3; i++){
        Item item = createItem();
        itemRepository.save(item);
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(10);
        orderItem.setOrderPrice(50000);
        orderItem.setOrder(order);
        order.getOrderItems().add(orderItem);
    }
        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadTest(){
        Order order = new Order();
        for(int i =0; i<3; i++){
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(50000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    @Test
    @DisplayName("고아 객체 삭제 테스트")
    public void orphanRemovalTest(){
        Order order= createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void LazyLoadingTest(){
        Order order = createOrder();
        Long orderItemId= order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem  = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("======orderItem.getOrder().getClass()======"+orderItem.getOrder().getClass());
        System.out.println("!!!!!!!!!!!!@!@!@!@!@!@!@!!!!@@@@@@@@@@@@@");
    orderItem.getOrder().getOrderDate();
        System.out.println("!@@!@!!@!@@@@!!!!!!!!!!!!!!!!!!@@@@@@@@@@!");
    }
}
