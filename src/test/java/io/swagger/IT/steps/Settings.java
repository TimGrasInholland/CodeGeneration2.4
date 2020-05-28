package io.swagger.IT.steps;

public class Settings {

    private String baseURL;
    private String headerName;
    private String authKey;

    private static Settings INSTANCE;

    private Settings() {
        this.baseURL = "http://localhost:8080/api";
        this.headerName = "session";
        this.authKey = "testEmployee";
    }

    public static Settings getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    public String getBaseURL() {
        return baseURL;
    }
}