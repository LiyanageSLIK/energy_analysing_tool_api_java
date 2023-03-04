package com.greenbill.greenbill.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenbill.greenbill.dto.refactor.BaseDto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
public class TreeViewReqResDto extends BaseDto {
    @NotNull
    private long projectId;
    @NotNull
    private JsonNode json;

//    public TreeViewReqResDto(TreeViewEntity treeViewEntity) throws JsonProcessingException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        this.json= objectMapper.readTree(treeViewEntity.getJson());
//    }
}
