package com.cs411.solar_project.controller;

import com.cs411.solar_project.model.Company;
import com.cs411.solar_project.model.Order;
import com.cs411.solar_project.service.SearchService;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SearchController {
    private SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping(value = "/search")
    public Order searchOrder(@RequestParam(name = "user_address") String usersAddress,
                                     @RequestParam(name = "bill") double bill) throws IOException, JSONException {

        List<List<String>> companies_info = searchService.getCompanyInfo(usersAddress);

        List<Company> companyList = new ArrayList<Company>();
        for(List<String> company_info : companies_info){
            Company.Builder builder = new Company.Builder();
            companyList.add(new Company(builder
                    .setCompanyName(company_info.get(0))
                    .setCompanyAddress(company_info.get(1))
                    .setCompanyWebsite(company_info.get(2))
                    .setCompanyRating(company_info.get(3))
                    .setCompanyPhoneNumber(company_info.get(4))
            ));
        }

        List<Double> latNLng = searchService.getLatNLng(usersAddress);

        double saving = searchService.getSavingAmount(bill);

        Order.Builder builder = new Order.Builder();
        Order newOrder = new Order(builder
                .setUserAddress(usersAddress)
                .setBillNumber(bill)
                .setSaving(saving)
                .setCompanyID(companyList)
        );

        return newOrder;
    }
}
