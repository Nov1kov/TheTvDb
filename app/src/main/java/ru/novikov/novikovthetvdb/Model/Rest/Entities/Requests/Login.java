package ru.novikov.novikovthetvdb.Model.Rest.Entities.Requests;


public class Login {

    private String apikey;
    private String username;
    private String userkey;

    public Login(String apikey, String username, String userkey){
        this.apikey = apikey;
        this.username = username;
        this.userkey = userkey;
    }
}
