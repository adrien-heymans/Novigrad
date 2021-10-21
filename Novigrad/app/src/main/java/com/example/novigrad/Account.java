//But: représenter tous les types de comptes

package com.example.novigrad;

public abstract class Account {
    //variables globales qui contiennent l'information de chaque compte
    private String fname;
    private String lname;
    private String email;
    private String password;
    private String phone;
    private String type;

    public Account(String fname,String lname, String phone,String email,String password, String type){
        //défini chaque variable
        //les tests pour s'assurer que le format et le contenu sont corrects
        //sont éffectués dans Login.java
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    //fonctions pour retourner le contenu des variables
    public String getFName(){
        return this.fname;
    }
    public String getType(){
        return this.type;
    }
    public String getLName(){
        return this.lname;
    }
    public String getPhone(){
        return this.phone;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }

    //fonctions pour modifier le contenu des variables
    public void setFName(String n){
        this.fname = n;
    }
    public void setLName(String n){
        this.lname = n;
    }
    public void setEmail(String n){
        this.email = n;
    }
    public void setPhone(String n){
        this.phone = n;
    }
    public void setPassword(String n){
        this.password = n;
    }
    public void setType(String n){
        this.type = n;
    }
}
