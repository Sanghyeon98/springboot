package com.keduit.dto;

import com.keduit.constant.ItemSellStatus;
import com.keduit.entity.Item;
import com.keduit.entity.ItemImg;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDTO {
    private Long id;
    private String itemNm;
    private String price;
    private String itemDetail;
    private Integer stockNumber;
    private ItemSellStatus itemSellStatus;
    private List<ItemImgDTO> itemImgDTOList = new ArrayList<>();
    private List<Long> itemImgIds = new ArrayList<>();
    private static ModelMapper modelMapper =new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this,Item.class);
    }

    public static ItemFormDTO of(Item item){
        return modelMapper.map(item,ItemFormDTO.class);
    }


}
