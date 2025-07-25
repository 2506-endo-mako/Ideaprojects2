package com.example.forum.service;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.CommentRepository;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Comment;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    /*
     *コメント返信全件取得処理
     */
    public List<CommentForm> findAllComment() {
        List<Comment> results = commentRepository.findAllByOrderByIdDesc();
        List<CommentForm> comments = setCommentForm(results);
        return comments;
    }


    /*
     * DBから取得したデータをFormに設定
     */
    private List<CommentForm> setCommentForm(List<Comment> results) {
        List<CommentForm> comments = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            CommentForm comment = new CommentForm();
            Comment result = results.get(i);
            comment.setId(result.getId());
            comment.setComment(result.getComment());
            comment.setContentId(result.getContentId());
            comment.setCreateDate(result.getCreateDate());
            comment.setUpdateDate(result.getUpdateDate());
                comments.add(comment);
        }
        return comments;
    }

    /*
     * コメント返信追加
     */
    public void saveComment(CommentForm reqComment) {
        Comment saveComment = setCommentEntity(reqComment);
        commentRepository.save(saveComment);
    }

    /*
     * コメント返信1件取得処理
     */
    public CommentForm editComment(Integer id) {
        List<Comment> results = new ArrayList<>();
        results.add((Comment) commentRepository.findById(id).orElse(null));
        List<CommentForm> comments = setCommentForm(results);
        return comments.get(0);
    }

    /*
     * コメント削除（Entityに詰めなくてよい）
     */
    public void deleteComment(Integer id) {
        commentRepository.deleteById(id);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Comment setCommentEntity(CommentForm reqComment) {
        Comment comment = new Comment();
        comment.setId(reqComment.getId());
        comment.setComment(reqComment.getComment());
        comment.setContentId(reqComment.getContentId());
        comment.setCreateDate(reqComment.getCreateDate());
        comment.setUpdateDate(reqComment.getUpdateDate());
        return comment;
    }



}

