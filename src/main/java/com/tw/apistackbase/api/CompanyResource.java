package com.tw.apistackbase.api;

import com.tw.apistackbase.core.CompanyRepository;
import com.tw.apistackbase.core.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/companies")
public class CompanyResource {
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping(produces = {"application/json"})
    public Iterable<Company> list() {
        return companyRepository.findAll();
    }

    @PostMapping(produces = {"application/json"})
    public void add(@RequestBody Company company) {
        companyRepository.save(company);
    }
}
