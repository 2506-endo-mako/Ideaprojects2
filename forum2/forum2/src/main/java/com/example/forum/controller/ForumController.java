package com.example.forum.controller;

import com.example.forum.controller.form.CommentForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentService;
import com.example.forum.service.ReportService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;
    @Autowired
    CommentService commentService;
    @Autowired
    HttpSession session;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    //@RequestParam（name）型　変数　→　ブラウザからのリクエストの値（パラメータ）を取得することができるアノテーション。
    //デフォルトでは値の入力は必須なので、required=falseがないとエラーになる
    public ModelAndView top(@RequestParam(name = "start_date", required = false) String startDate,
                            @RequestParam(name = "end_date", required = false) String endDate) {
        ModelAndView mav = new ModelAndView();
        // 投稿を全件取得
        List<ReportForm> contentData = reportService.findAllReport(startDate, endDate);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("contents", contentData);

        Integer reportId2 = (Integer)session.getAttribute("reportId");
        mav.addObject("reportId", reportId2);

        String message2 = (String)session.getAttribute("message");
        mav.addObject("message", message2);
        session.invalidate();

        //コメント内容表示処理
        // 投稿を全件取得
        List<CommentForm> commentData = commentService.findAllComment();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // コメント返信オブジェクトを保管
        mav.addObject("comments", commentData);
        //コメント返信用に空のcommentFormを準備
        mav.addObject("formModel", new CommentForm());
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        //mav.addObject("message", message);
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 投稿編集画面表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * コメント返信編集画面表示
     */
    @GetMapping("/commentEdit/{id}")
    public ModelAndView editCommentt(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集するコメントを取得
        CommentForm comment = commentService.editComment(id);
        // 編集する投稿をセット
        mav.addObject("formModel", comment);
        // 画面遷移先を指定
        mav.setViewName("/commentEdit");
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel")
                                   @Validated ReportForm reportForm, BindingResult result) {
        if (result.hasErrors()) {
            String message = null;
            for (FieldError error : result.getFieldErrors()) {
                message = error.getDefaultMessage();
            }
            ModelAndView mav = new ModelAndView();
            mav.addObject("message", message);
            mav.setViewName("/new");
            return mav;
        }

        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * コメント返信投稿処理
     */
    @PostMapping("/commentAdd")
    public ModelAndView addComment(@Validated @ModelAttribute("formModel") CommentForm commentForm,
                                   BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                String message = error.getDefaultMessage();

                ModelAndView mav = new ModelAndView();
                int reportId = commentForm.getContentId();
                session.setAttribute("reportId", reportId);
                session.setAttribute("message", message);
                // rootへリダイレクト
                return new ModelAndView("redirect:/");
            }
        }
        // 投稿をテーブルに格納
        commentService.saveComment(commentForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *コメント削除処理
     */
    @DeleteMapping("/commentDelete/{id}")
    public ModelAndView deleteComment(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        commentService.deleteComment(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *投稿編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id,
                                      @Validated @ModelAttribute("formModel") ReportForm report, BindingResult result) {
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                String message = error.getDefaultMessage();

                ModelAndView mav = new ModelAndView();
                mav.addObject("message", message);
                // 画面遷移先を指定
                mav.setViewName("/edit");
                return mav;
            }
        }
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *コメント編集処理
     */
    @PutMapping("/commentUpdate/{id}")
    public ModelAndView updateCommennt(@PathVariable Integer id,
                                       @Validated @ModelAttribute("formModel") CommentForm comment, BindingResult result) {
        if(result.hasErrors()) {
            for(FieldError error : result.getFieldErrors()) {
                String message = error.getDefaultMessage();

                ModelAndView mav = new ModelAndView();
                mav.addObject("message", message);
                mav.setViewName("/commentEdit");
                return mav;

            }
        }

        // UrlParameterのidを更新するentityにセット
        comment.setId(id);
        // 編集した投稿を更新
        commentService.saveComment(comment);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}

