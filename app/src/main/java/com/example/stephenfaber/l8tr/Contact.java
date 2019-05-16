package com.example.stephenfaber.l8tr;

public class Contact
{
    String Id;
    String Name;
    String Phone;
    String Email;

    public Contact() {

    }

    // Setters
    public void setId(String id) {
        this.Id = id;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public void setPhone(String phone) {
        this.Phone = phone;
    }
    public void setEmail(String email) {
        this.Email = email;
    }

    // Getters
    public String getId() {
        return Id;
    }
    public String getEmail() {
        return Email;
    }
    public String getPhone() {
        return Phone;
    }
    public String getName() {
        return Name;
    }





}
