package com.keduit.repository;

import com.keduit.constant.ItemSellStatus;
import com.keduit.entity.Item;
import com.keduit.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
@Slf4j
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Querydsl 테스트")
    public void querydslTest(){
        this.createItemList();

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory
                .select(qItem)
                .from(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for (Item item : itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("Querydsl 테스트2")
    public void querydslTest2(){
        BooleanBuilder builder = new BooleanBuilder();
        QItem item = QItem.item;
        String itemDetail = "테스트";
        int price = 50001;
        String itemSellStat = "SELL";

        builder.and(item.itemDetail.like("%" + itemDetail + "%"));
        builder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat, ItemSellStatus.SELL)){
            builder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> result = itemRepository.findAll(builder,pageable);

        System.out.println("전체 페이지 수 = "+result.getTotalPages());
        System.out.println("조회한 전체 상품수 = "+result.getTotalElements());
        System.out.println("현재 페이지의 게시물수 = "+result.getSize());
        System.out.println("현재 페이지 수 = "+result.getNumber());
        System.out.println(" content = "+result.getContent());
    }


    @Test
    @DisplayName("상품 저장 테스트")
    public void createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(50000);
        item.setItemDetail("상품상세정보임!");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(10000);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item saveItem=itemRepository.save(item);
        itemRepository.findAll();
        System.out.println("saveItem = " + saveItem);

    }

    @Test
    public void findByItemNmTest(){
        this.createItemList();

        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for(Item item : itemList){
            System.out.println("item = " + item);
        }

    }

    @Test
    public void createItemList(){
        for (int i=1; i<=21; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 * i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            if (i < 11){
                item.setItemSellStatus(ItemSellStatus.SELL);
                item.setStockNumber(1000);
            }else {
                item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
                item.setStockNumber(0);
            }
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item savedItem = itemRepository.save(item);
        }
    }

    @Test
    public void findByItemNmOrItemDetailTest(){
        this.createItemList();

        List<Item> itemList =
                itemRepository.findByItemNmOrItemDetail("테스트 상품2","상품상세정보임!2");
        
        for (Item item : itemList){
            System.out.println("item = " + item);
        }


    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThantest() {

        this.createItemList();
        List<Item> itemList = itemRepository.findByPriceLessThan(50004);
        for (Item item : itemList) {
            System.out.println(item);

        }

    }

    @Test
    public void findByPriceLessThanOrderByPriceDescTest (){
        this.createItemList();
        
        List<Item> itemList = 
                itemRepository.findByPriceLessThanOrderByPriceDesc(50004);
        for(Item item : itemList){
            System.out.println("item = " + item);
        }
        
    }
    



    @Test
    public void testSelect(){
        this.createItemList();
        Long id = 10L;

        Optional<Item> result = itemRepository.findById(id);
        System.out.println("===============================");
        if(result.isPresent()){
            Item item = result.get();
            System.out.println("item = " + item);
        }
    }

//    @Transactional
//    @Test
//    public void testSelect2(){
//        this.createItemList();
//        Long id = 10L;
//
//        Item item = itemRepository.getOne(id);
//        System.out.println("=============================");
//        System.out.println("item = " + item);
//    }
    
    @Test
    public void testUpdate(){
        Item item = Item.builder().id(10L).itemNm("수정된 상품명").itemDetail("수정된 상세").price(25000).build();
        System.out.println("itemRepository.save(item).toString() = " + itemRepository.save(item).toString());
        
    }

//    @Test
//    public void testDelete(){
//        Long id =10L;
//        itemRepository.deleteById(id);
//    }

    @Test
    public void testPageDefault(){
        Pageable pageable = PageRequest.of(3,5);
        Page<Item> result = itemRepository.findAll(pageable);
        System.out.println("result = " + result);
        System.out.println("===================================");
        for(Item item : result.getContent()){
            System.out.println("item = " + item);
        }

    }
    
    @Test
    public void testSort(){
        Sort sort1 = Sort.by("id").descending();
        Pageable pageable =
                PageRequest.of(0,10,sort1);
        Page<Item> result =itemRepository.findAll(pageable);
        result.get().forEach(item ->{
            System.out.println("item = " + item);
            //log.info(item.toString());
        });
    }
    @Test
    @DisplayName("Query를 이용한 상품조회 테스트")
    public void findItemDetailTest(){
        this.createItemList();
        Pageable pageable = PageRequest.of(1,10, Sort.by("price").descending());
        List<Item> itemList = itemRepository.findByItemDetail("상세", pageable);
        for (Item item: itemList){
            System.out.println("item = " + item);
        }
    }

    @Test
    @DisplayName("nativeQuery 속성을 이용한 상품조회 테스트")
    public void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemByNative("상세");
        for(Item item : itemList){
            System.out.println("item = " + item);
        }
    }




}