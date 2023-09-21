package com.keduit.repository;

import com.keduit.constant.ItemSellStatus;
import com.keduit.entity.Item;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

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

    private void createItemList() {

        for(int i=1; i<11; i++){
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(50000 + i);
            item.setItemDetail("상품상세정보임!"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(10000);
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            Item saveItem=itemRepository.save(item);
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

    @Transactional
    @Test
    public void testSelect2(){
        this.createItemList();
        Long id = 10L;
        
        Item item = itemRepository.getOne(id);
        System.out.println("=============================");
        System.out.println("item = " + item);
    }
    
    @Test
    public void testUpdate(){
        Item item = Item.builder().id(10L).itemNm("수정된 상품명").itemDetail("수정된 상세").price(25000).build();
        System.out.println("itemRepository.save(item).toString() = " + itemRepository.save(item).toString());
        
    }

    @Test
    public void testDelete(){
        Long id =10L;
        itemRepository.deleteById(id);
    }

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
            log.info(item.toString());
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