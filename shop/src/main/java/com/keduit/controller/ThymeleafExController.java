package com.keduit.controller;

import com.keduit.constant.ItemSellStatus;
import com.keduit.dto.ItemDto;
import com.keduit.entity.Item;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/thymeleaf")
@Slf4j
public class ThymeleafExController {

    @GetMapping("/ex01")
    public String thymeleafEx01(Model model){
        model.addAttribute("data","타임리프 예제입니다.");
        return "thymeleafEX/thymeleafEx01";
    }

    @GetMapping("/ex02")
    public String thymeleafExample02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto",itemDto);
        return "thymeleafEX/thymeleafEx02";
    }

    @GetMapping("/ex03")
    public String thymeleafEx03(Model model){
        List<ItemDto> itemDTOList = new ArrayList<>();

        for(int i= 1; i<21; i++){
            ItemDto item = new ItemDto();

            item.setId((long)i);
            item.setItemNm("테스트 상품" + i);
            item.setPrice(50000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            if (i < 11){
                item.setSellStatCd("SELL");
            }else {
                item.setSellStatCd("SOLD_OUT");
            }
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemDTOList.add(item);
            System.out.println("item = " + item);
        }
        model.addAttribute("itemDTOList",itemDTOList);

        return "thymeleafEX/thymeleafEx03";
    }
    @GetMapping("/ex04")
    public String thymeleafEx04(Model model){
        List<ItemDto> itemDTOList = new ArrayList<>();

        for(int i= 1; i<21; i++){
            ItemDto item = new ItemDto();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(50000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            if (i < 11){
                item.setSellStatCd("SELL");
            }else {
                item.setSellStatCd("SOLD_OUT");
            }
            item.setRegTime(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemDTOList.add(item);
            System.out.println("item = " + item);
        }
        model.addAttribute("itemDTOList",itemDTOList);

        return "thymeleafEX/thymeleafEx04";
    }

    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        return "thymeleafEX/thymeleafEx05";
    }

    @GetMapping("/ex06")
    public String thymeleafEx06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEX/thymeleafEx06";

    }
    @GetMapping("ex07")
    public String thymeleafEx07(){
        return "thymeleafEX/thymeleafEx07";
    }

    @GetMapping("/exInline")
    public String exInline(RedirectAttributes redirectAttributes){
        log.info("............exInline==========");

        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());
        redirectAttributes.addFlashAttribute("result", "success");
        redirectAttributes.addFlashAttribute("dto",itemDto);
        return "redirect:/thymeleaf/thymeleafEx08";
    }

    @GetMapping("/thymeleafEx08")
    public String ex08(){
        System.out.println("ex08");
        return "thymeleafEX/thymeleafEx08";
    }
}
