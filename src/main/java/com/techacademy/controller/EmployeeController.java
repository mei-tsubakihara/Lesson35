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

import com.techacademy.entity.Authentication;
import com.techacademy.entity.Employee;
import com.techacademy.service.AuthenticationService;
import com.techacademy.service.EmployeeService;



@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final EmployeeService service;
    private final AuthenticationService aservice;
    //authenticationserviceを作成→認証情報のテーブルを取得しやすくするため

    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeService service, AuthenticationService aservice) {
        this.service = service;
        this.aservice = aservice;
    }

    // ----- 一覧画面 -----
    @GetMapping("/list")
    public String getList(Model model) {
        model.addAttribute("employeeList", service.getEmployeeList());
        model.addAttribute("listSize", service.getEmployeeList().size());
        //EmployeeList.htmlに画面遷移
        return "employee/list";
    }

    // ----- 詳細画面 -----
    @GetMapping(value = { "/detail", "/detail/{id}/" })
    public String getEmployee(@PathVariable(name = "id", required = false) Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のオブジェクトを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        // Modelに登録
        model.addAttribute("employee", employee);
        // employee/detail.htmlに画面遷移
        return "employee/detail";
    }

     // ----- 登録画面 -----
    @GetMapping("/register")
    public String getRegister(@ModelAttribute Employee employee) {
        //登録画面に遷移
        return "employee/register";
    }

    // ----- 登録処理 -----
    @PostMapping("/register")
    public String postRegister(@Validated Employee employee, BindingResult res, Model model) {

        if(res.hasErrors() || employee.getAuthentication().getRole().equals("")) {
            //@Notnullでは一般・管理者・選択されていないの3つのうち「選択されていない」を判定できない→空の場合のエラー処理を記載
            // エラーあり
            return getRegister(employee);
        }

        Authentication authentication = aservice.getAuthentication(employee.getAuthentication().getCode());
        if ( authentication != null ) {
            //画面側の社員番号とテーブル側の社員番号を比較してnullじゃなければ
            return "employee/register";
            //登録画面に戻る（登録できない）
            //エラーメッセージを追加するのもあり
            //returnで処理を終了させているのでelseを書く必要なし
        }

        LocalDateTime now = LocalDateTime.now();
        employee.setCreatedAt(now);
        employee.setUpdatedAt(now);
        employee.setDelete_flag(0);
        employee.getAuthentication().setEmployee(employee);

        String registerpassword = "";
        registerpassword = employee.getAuthentication().getPassword();
        employee.getAuthentication().setPassword(passwordEncoder.encode(registerpassword));
        //登録
        service.saveEmployee(employee);
        //一覧画面にリダイレクト
        return "redirect:/employee/list";
    }

     // ----- 更新画面 -----
    @GetMapping("/update/{id}")
    public String getEmployeeUpdate(@PathVariable("id") Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のオブジェクトを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        employee.getAuthentication().setPassword("");
        // Modelに登録
        model.addAttribute("employee", employee);
        // employee/update.htmlに画面遷移
        return "employee/update";
    }

    // ----- 更新処理 -----
    @PostMapping("/update/{id}/")
    public String postEmployeeUpdate(Employee employee) {
        LocalDateTime now = LocalDateTime.now();
        employee.setCreatedAt(now);
        employee.setUpdatedAt(now);
        employee.setDelete_flag(0);
        employee.getAuthentication().setEmployee(employee);

        String password = "";
        //空の変数を用意して分岐によって変数を決める
        if ( employee.getAuthentication().getPassword().equals("")) {
            Employee tableEmployee = service.getEmployee(employee.getId());
            //table側から従業員情報をもってくる（サービス経由）
            password = tableEmployee.getAuthentication().getPassword();
            employee.getAuthentication().setPassword(password);
            //登録処理で暗号化されているものをそのまま登録

        } else {
            password = employee.getAuthentication().getPassword();
            employee.getAuthentication().setPassword(passwordEncoder.encode(password));
            //暗号化されていないので暗号化処理を実施
        }

        //登録
        service.saveEmployee(employee);
        //一覧画面にリダイレクト
        return "redirect:/employee/list";
    }

    // ----- 削除処理 -----
    @GetMapping("/delete/{id}")
    public String getEmployeeDelete(@PathVariable("id") Integer id, Model model) {
        // codeが指定されていたら検索結果、無ければ空のオブジェクトを設定
        Employee employee = id != null ? service.getEmployee(id) : new Employee();
        employee.setDelete_flag(1);
        //登録
        service.saveEmployee(employee);
        //一覧画面にリダイレクト
        return "redirect:/employee/list";
    }
}
