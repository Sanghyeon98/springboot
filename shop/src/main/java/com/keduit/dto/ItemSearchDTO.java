package com.keduit.dto;

import com.keduit.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDTO {

    private String searchDateType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery ="";
}
