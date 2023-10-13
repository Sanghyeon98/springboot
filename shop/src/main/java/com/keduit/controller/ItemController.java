package com.keduit.controller;

import com.keduit.dto.ItemFormDTO;
import com.keduit.dto.ItemSearchDTO;
import com.keduit.entity.Item;
import com.keduit.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDTO itemFormDto, BindingResult bindingResult, Model model
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        //상품 등록시 필수 데이터가 없으면 상품등록페이지로 돌아감
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        //첫번째 이미지가 없다면 에러메세지와 함께 상품 등록 페이지로 전환. 상품의 첫번째 이미지는 메인에 필요하기에 필수 값 지정
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        //상품 저장 로직 호출. 매개 변수로 상품 정보와 상품 이미지 정보를 담고있는 itemImgFileList로 넘겨줌
        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        //정상적으로 실행되면 메인 페이지로 이동
        return "redirect:/";
    }

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDTO" , new ItemFormDTO());
        //new ItemFormDTO 비어있는 객체를 보내면서 화면에 빈값을 넣어줌
        return "/item/itemForm";
    }

    @GetMapping("/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId ,Model model){

        try{
            ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDTO",itemFormDTO);
        }catch (Exception e){
            model.addAttribute("errorMessage","존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDTO", new ItemFormDTO());
            return "item/itemForm";
        }
        return "item/itemForm";
    }


    @PostMapping("/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, RedirectAttributes re, Model model){

        if (bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if (itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDTO, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정중 에러 발생!");
            return "item/itemForm";
        }

        re.addFlashAttribute("result", "itemModifySuccess");

        return "redirect:/";
    }
    @GetMapping(value = {"/admin/items","/admin/items/{page}"})
    public String itemManage(ItemSearchDTO itemSearchDTO,
                             @PathVariable("page")Optional<Integer> page,
                             Model model){

        Pageable pageable = PageRequest.of(page.isPresent()? page.get() : 0 , 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDTO,pageable);
        model.addAttribute("items",items);
        model.addAttribute("itemSearchDTO",itemSearchDTO);
        model.addAttribute("maxPage",5);
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDTO itemFormDTO = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDTO);
        return "item/itemDtl";
    }
}
