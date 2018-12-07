package com.tw.apistackbase.api;

import com.tw.apistackbase.core.Employee;
import com.tw.apistackbase.core.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = {"http://localhost:3000","https://parkingwebappmobile.herokuapp.com","https://binluis-parkingwebapp.herokuapp.com"})
@RequestMapping("/employees")
public class EmployeeResource {
    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping(produces = {"application/json"})
    public Iterable<Employee> list() {
        return employeeRepository.findAll();
    }

    @PostMapping(produces = {"application/json"})
    public void add(@RequestBody Employee employee) {
        employeeRepository.save(employee);
    }

    @GetMapping(path = "/abc",produces = {"application/json"})
    public Iterable<Employee> listabc() {
        ArrayList<Employee> employees=new ArrayList<Employee>();
        employees.add(new Employee("abc",25));
        return employees;
    }
}
