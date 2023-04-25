package com.techacademy.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.techacademy.entity.Employee;
import com.techacademy.entity.Report;
import com.techacademy.repository.ReportRepository;

@Service
public class ReportService {
    private final ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public List<Report> getReportList(){
        return reportRepository.findAll();
    }


    //ログインしている従業員の日報一覧をとってくるためのメソッド
    public List<Report> getLgReportList(Employee employee){
        return reportRepository.findByEmployee(employee);
    }
    //Report.javaでは、employee_idをemployeeとして定義しているので、findByのあとはEmployeeでemployee_idを取得することができる
    //@AuthenticationPrincipalはコントローラーでしか使えない

    // 1件を検索して返す（登録）
    public Report getReport(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

    //登録
    @Transactional
    public Report saveReport(Report report) {
        return reportRepository.save(report);
    }

    // 1件を検索して返す（更新）
    public Report getReportUpdate(Integer id) {
        // findByIdで検索
        Optional<Report> option = reportRepository.findById(id);
        // 取得できなかった場合はnullを返す
        Report report = option.orElse(null);
        return report;
    }

}