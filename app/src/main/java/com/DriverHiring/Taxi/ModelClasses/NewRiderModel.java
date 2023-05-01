package com.DriverHiring.Taxi.ModelClasses;

public class NewRiderModel {
    String age,city,cnicno,currentlatitude,currentlogitude,designetion,email,fullname,gender,online,phoneno,profileimageurl,provence,reported,status,uid,verified;

    public NewRiderModel()
    {

    }

    public NewRiderModel(String age, String city, String cnicno, String currentlatitude, String currentlogitude, String designetion, String email, String fullname, String gender, String online, String phoneno, String profileimageurl, String provence, String reported, String status, String uid, String verified) {
        this.age = age;
        this.city = city;
        this.cnicno = cnicno;
        this.currentlatitude = currentlatitude;
        this.currentlogitude = currentlogitude;
        this.designetion = designetion;
        this.email = email;
        this.fullname = fullname;
        this.gender = gender;
        this.online = online;
        this.phoneno = phoneno;
        this.profileimageurl = profileimageurl;
        this.provence = provence;
        this.reported = reported;
        this.status = status;
        this.uid = uid;
        this.verified = verified;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCnicno() {
        return cnicno;
    }

    public void setCnicno(String cnicno) {
        this.cnicno = cnicno;
    }

    public String getCurrentlatitude() {
        return currentlatitude;
    }

    public void setCurrentlatitude(String currentlatitude) {
        this.currentlatitude = currentlatitude;
    }

    public String getCurrentlogitude() {
        return currentlogitude;
    }

    public void setCurrentlogitude(String currentlogitude) {
        this.currentlogitude = currentlogitude;
    }

    public String getDesignetion() {
        return designetion;
    }

    public void setDesignetion(String designetion) {
        this.designetion = designetion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public void setProfileimageurl(String profileimageurl) {
        this.profileimageurl = profileimageurl;
    }

    public String getProvence() {
        return provence;
    }

    public void setProvence(String provence) {
        this.provence = provence;
    }

    public String getReported() {
        return reported;
    }

    public void setReported(String reported) {
        this.reported = reported;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }
}
