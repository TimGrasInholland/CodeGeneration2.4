package io.swagger.IT;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

public class BaseClassTesting {

    public HttpHeaders headers = new HttpHeaders();
    public RestTemplate template = new RestTemplate();
    public ObjectMapper mapper = new ObjectMapper();
    public Settings settings = Settings.getInstance();
    public String baseUrl = settings.getBaseURL();
    public HttpEntity<String> httpEntity;
    public ResponseEntity<String> responseEntity;
    public URI uri;

    public BaseClassTesting() {
        headers.add(settings.getHeaderName(), settings.getAuthKey());
    }
}
