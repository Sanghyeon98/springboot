package com.keduit.dto;

import com.keduit.entity.ItemImg;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class ItemImgDTO {


    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper();
    public static ItemImgDTO of(ItemImg itemImg){
        return modelMapper.map(itemImg,ItemImgDTO.class);
    }

}
