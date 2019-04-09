package com.cdtlab19.coffeeapi.domain;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

import java.io.Serializable;
import java.util.Set;

public class Identity implements User, Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Set<String> roles;
    private String affiliation;
    private Enrollment enrollment;
    private String mspId;

    public Identity() { }

    public Identity(String name, String affiliation, String mspId, Enrollment enrollment) {
        this.name = name;
        this.affiliation = affiliation;
        this.mspId = mspId;
        this.enrollment = enrollment;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    @Override
    public String getMspId() {
        return mspId;
    }

    public void setMspId(String mspId) {
        this.mspId = mspId;
    }

    @Override
    public String getAccount() {
        return null;
    }
}
