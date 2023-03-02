package com.greenbill.greenbill.entity;

import com.fasterxml.jackson.databind.JsonNode;
import com.greenbill.greenbill.dto.TreeViewReqResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tree_view",uniqueConstraints = @UniqueConstraint(columnNames = {"user_email", "project_id"}))
public class TreeViewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email",nullable = false)
    private String userEmail;
    @Column(name = "project_id", nullable = false)
    private long projectId;

    @Column(name = "json",columnDefinition="LONGTEXT", nullable = false)
    private String json;

    public TreeViewEntity(TreeViewReqResDto treeViewReqResDto,String email) {
        this.userEmail=email;
        this.projectId= treeViewReqResDto.getProjectId();
        this.json= treeViewReqResDto.getJson().toString();
    }
}
