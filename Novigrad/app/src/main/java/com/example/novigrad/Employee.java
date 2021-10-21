

//But: représenter un employé

package com.example.novigrad;

public class Employee extends Account{

    //construit l'objet grâce à la classe Account
    public Employee (String fname,String lname, String phone,String email,String password){
        super(fname, lname, phone, email, password, "Employee");
    }
}