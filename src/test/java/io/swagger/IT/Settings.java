package io.swagger.IT;

public class Settings {

    private static Settings INSTANCE;
    private String baseURL;
    private String headerName;
    private String authKey;

    private Settings() {
        this.baseURL = "http://localhost:8080/api";
        this.headerName = "session";
        this.authKey = "40e7a688-a0da-11ea-bb37-0242ac130002";
    }

    public static Settings getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Settings();
        }
        return INSTANCE;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public String getHeaderName() {
        return headerName;
    }

    public String getAuthKey() {
        return authKey;
    }
}