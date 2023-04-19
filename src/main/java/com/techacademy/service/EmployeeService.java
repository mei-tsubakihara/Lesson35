package com.techacademy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.entity.Employee;
import com.techacademy.repository.EmployeeRepository;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getEmployeeList(){
        return employeeRepository.findAll();
    }

    // 1件を検索して返す（登録）
    public Employee getEmployee(Integer id) {
        // findByIdで検索
        Optional<Employee> option = employeeRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Employee employee = option.orElse(null);
        return employee;
    }

    //登録
    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // 1件を検索して返す（更新）
    public Employee getEmployeeUpdate(Integer id) {
        // findByIdで検索
        Optional<Employee> option = employeeRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Employee employee = option.orElse(null);
        return employee;
    }


}