package com.techacademy.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.techacademy.entity.Report;
import com.techacademy.service.ReportService;



@Controller
@RequestMapping("report")
public class ReportController {
    private final ReportService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReportController(ReportService service) {
        this.service = service;
    }

    // ----- 一覧画面 -----
    @GetMapping("/list")
    public String getList(Model model) {
        model.addAttribute("reportList", service.getReportList());
        model.addAttribute("listSize", service.getReportList().size());
        //report/list.htmlに画面遷移
        return "report/list";
    }


    // ----- 詳細画面 -----
    @GetMapping(value = { "/detail", "/detail/{id}/" })
    public String getReport(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のオブジェクトを設定
        Report report = id != null ? service.getReport(id) : new Report();
        // Modelに登録
        model.addAttribute("report", report);
        // report/detail.htmlに画面遷移
        return "report/detail";
    }

    // ----- 登録画面 -----
   @GetMapping("/register")
   public String getRegister(@ModelAttribute Report report) {
       //登録画面に遷移
       return "report/register";
   }

   // ----- 登録処理 -----
   @PostMapping("/register")
   public String postRegister(@Validated Report report, BindingResult res, Model model) {

       if(res.hasErrors()) {
           // エラーあり
           return getRegister(report);
       }

       LocalDateTime now = LocalDateTime.now();
       report.setCreatedAt(now);
       report.setUpdatedAt(now);

       //登録
       service.saveReport(report);
       //一覧画面にリダイレクト
       return "redirect:/report/list";
   }
}