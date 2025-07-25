package com.example.forum.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "comment2")
@Getter
@Setter
public class Comment {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String comment;

    @Column(name = "content_id",insertable = true, updatable = false)
    private int contentId;

    @Column(name = "create_date", insertable = false, updatable = false)
    private Date createDate;

    @Column(name = "update_date", insertable = false, updatable = false)
    private Date updateDate;
}

