package com.example.forum.controller.form;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommentForm {

    private int id;
    private String comment;

    @Column(name = "create_date",insertable = false, updatable = true)
    private int contentId;

    @Column(name = "create_date",insertable = false, updatable = false)
    private Date createDate;

    @Column(name = "update_date",insertable = false, updatable = false)
    private Date updateDate;

}

