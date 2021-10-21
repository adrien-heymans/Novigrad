

//But: représenter un client

package com.example.novigrad;

public class Customer extends Account{

    //construit l'objet grâce à la classe Account
    public Customer (String fname,String lname, String phone,String email,String password){
        super(fname, lname, phone, email, password, "Customer");
    }
}