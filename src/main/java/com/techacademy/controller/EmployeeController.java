package com.techacademy.controller;

import java.time.LocalDateTime;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.techacademy.entity.Employee;
import com.techacademy.service.EmployeeService;



@Controller
@RequestMapping("employee")
public class EmployeeController {
    private final EmployeeService service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public EmployeeController(EmployeeService service) {
        this.service = service;
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

        if(res.hasErrors()) {
            // エラーあり
            return getRegister(employee);
        }

        LocalDateTime now = LocalDateTime.now();
        employee.setCreatedAt(now);
        employee.setUpdatedAt(now);
        employee.setDelete_flag(0);
        employee.getAuthentication().setEmployee(employee);
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
        employee.getAuthentication().setPassword(passwordEncoder.encode(employee.getAuthentication().getPassword()));
        //passwordをセット→暗号化したものに登録しなおす（employeeとして登録されているものからget）

        //PathVariableでidを取ってくる→47行目の処理を行う
        //passwordがnullかnullじゃないかで分かれる→nullだったらtableのemployeeからpasswordを持ってくる/nullじゃなかったら新しいpasswordを使用する（画面から取ってくる→こちらは暗号化処理が必要）

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
