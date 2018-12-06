package com.tw.apistackbase.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class CompanyProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long registeredCapital;
    private String certId;

    public long getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(long registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public CompanyProfile() {
    }
}
