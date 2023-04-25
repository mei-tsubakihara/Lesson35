package com.techacademy.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.techacademy.service.ReportService;
import com.techacademy.service.UserDetail;

@Controller
public class LoginController {

    //20230424追記→Reportserviceの初期化、宣言
    private final ReportService service;
    public LoginController(ReportService service) {
        this.service = service;
    }

    /** ログイン画面を表示 */
    @GetMapping("/login")
    public String getLogin() {
        // login.htmlに画面遷移
        return "login";
    }

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal UserDetail userDetail) {

        //findbyEmployeeメソッドを使う（サービス側で作成したメソッドを指定）
        model.addAttribute("reportList", service.getLgReportList(userDetail.getEmployee()));
        model.addAttribute("listSize", service.getLgReportList(userDetail.getEmployee()).size());
        //userDetailにあるemployeeからログインしている従業員情報をとってくる

        // index.htmlに画面遷移
        return "index";
        //トップページはindexにすることが多い
    }


}