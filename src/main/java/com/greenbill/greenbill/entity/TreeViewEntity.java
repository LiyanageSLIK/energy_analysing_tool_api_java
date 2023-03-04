package com.greenbill.greenbill.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenbill.greenbill.dto.TreeViewReqResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(name = "tree_view")
public class TreeViewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "json",columnDefinition="LONGTEXT", nullable = true)
    private String json;

//    @OneToOne(mappedBy = "treeView", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
//    private ProjectEntity project;

//    public TreeViewEntity(TreeViewReqResDto treeViewReqResDto,String email) {
//        this.json= treeViewReqResDto.getJson().toString();
//    }

}
