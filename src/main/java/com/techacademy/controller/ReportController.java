package com.techacademy.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import com.techacademy.service.UserDetail;



@Controller
@RequestMapping("report")
public class ReportController {
    private final ReportService service;

    @Autowired

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
    public String getReport(@PathVariable(name = "id", required = false) Integer id, Model model, @AuthenticationPrincipal UserDetail userDetail) {
        // codeが指定されていたら検索結果、無ければ空のオブジェクトを設定
        Report report = id != null ? service.getReport(id) : new Report();

        int flag = 0;

        if ( report.getEmployee().getId() == userDetail.getEmployee().getId()) {
            //日報の従業員IDとログインしている従業員のIDを比較（Integerなので==）
            flag = 1;
        }

        //idが他の従業員の情報も取ってこれるようになっている
        //userDetailの従業員情報とidの内容を比較して、一致していれば編集可能とする→htmlでif分岐を作成

        // Modelに登録
        model.addAttribute("report", report);
        model.addAttribute("flag", flag);
        // report/detail.htmlに画面遷移
        return "report/detail";
    }

    // ----- 登録画面 -----
   @GetMapping("/register")
   public String getRegister(@ModelAttribute Report report, @AuthenticationPrincipal UserDetail userDetail) {
       report.setEmployee(userDetail.getEmployee());
       //UserDetailで獲得した従業員情報を使うことができる→reportに従業員情報を登録する
       //userDetailのgetEmployeeメソッドを呼び出す
       //登録画面に遷移
       return "report/register";
   }

   // ----- 登録処理 -----
   @PostMapping("/register")
   public String postRegister(@Validated Report report, BindingResult res, Model model, @AuthenticationPrincipal UserDetail userDetail) {

       if(res.hasErrors()) {
           // エラーあり
           return "report/register";
       }

       LocalDateTime now = LocalDateTime.now();
       report.setCreatedAt(now);
       report.setUpdatedAt(now);
       report.setEmployee(userDetail.getEmployee());


       //登録
       service.saveReport(report);
       //一覧画面にリダイレクト
       return "redirect:/report/list";
   }


   // ----- 更新画面 -----
  @GetMapping("/update/{id}")
  public String getReportUpdate(@PathVariable("id") Integer id, Model model) {
      // codeが指定されていたら検索結果、無ければ空のオブジェクトを設定
      Report report = id != null ? service.getReport(id) : new Report();

      // Modelに登録
      model.addAttribute("report", report);
      // report/update.htmlに画面遷移
      return "report/update";
  }

  // ----- 更新処理 -----
  @PostMapping("/update/{id}/")
  public String postReportUpdate(@PathVariable("id") Integer id, Report report, @AuthenticationPrincipal UserDetail userDetail) {
      LocalDateTime now = LocalDateTime.now();

      report.setUpdatedAt(now);
      report.setEmployee(userDetail.getEmployee());

      Report tableReport = service.getReport(id);
      //table側から従業員情報をもってくる（サービス経由）PathVariableで取得したidを使用してサービスをもってくる
      report.setCreatedAt(tableReport.getCreatedAt());

      //登録
      service.saveReport(report);
      //一覧画面にリダイレクト
      return "redirect:/report/list";
  }

}