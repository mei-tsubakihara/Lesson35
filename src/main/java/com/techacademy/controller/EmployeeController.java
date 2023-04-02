package com.techacademy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.techacademy.service.EmployeeService;

@Controller
@RequestMapping("/")
public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String getList(Model model) {
        model.addAttribute("employeeList", service.getEmployeeList());
        //EmployeeList.htmlに画面遷移
        return "employee/list";
    }
}