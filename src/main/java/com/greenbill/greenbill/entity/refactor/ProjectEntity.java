package com.greenbill.greenbill.entity.refactor;

import com.greenbill.greenbill.enumeration.ProjectType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "project_type", nullable = false)
    private ProjectType projectType;

    @Column(name = "last_updated", nullable = false)
    private Date lastUpdated = new Date();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private RootEntity root;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private SubscriptionEntity subscription;
}
