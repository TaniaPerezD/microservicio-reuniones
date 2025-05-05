package online.sis_ucb.meetings_service.oauth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate; // O WebClient
import java.util.Base64;
import online.sis_ucb.meetings_service.model.ZoomTokenResponse;

@Service
public class ZoomOAuthService {
    @Value("${zoom.oauth.account-id}")
    private String accountId;

    @Value("${zoom.oauth.client-id}")
    private String clientId;

    @Value("${zoom.oauth.client-secret}")
    private String clientSecret;

    @Value("${zoom.oauth.token-url}")
    private String tokenUrl;

    private final RestTemplate restTemplate; // Inyectar RestTemplate

    private ZoomTokenResponse currentToken;
    private long tokenExpiryTime;

    public ZoomOAuthService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAccessToken() {
        if (currentToken == null || System.currentTimeMillis() >= tokenExpiryTime) {
            refreshToken();
        }
        return currentToken.getAccess_token();
    }

    private void refreshToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        // Codificación Base64 de client_id:client_secret
        String authHeader = clientId + ":" + clientSecret;
        headers.setBasicAuth(clientId, clientSecret); // RestTemplate lo maneja directamente

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "account_credentials");
        map.add("account_id", accountId);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        try {
            ResponseEntity<ZoomTokenResponse> response = restTemplate.postForEntity(tokenUrl, request, ZoomTokenResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                currentToken = response.getBody();
                tokenExpiryTime = System.currentTimeMillis() + (currentToken.getExpires_in() * 1000) - 5000; // Buffer
            } else {
                // Considerar lanzar una excepción más específica o manejar el error
                throw new RuntimeException("Failed to get Zoom OAuth token: " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (Exception e) {
            // Manejar excepciones de conexión, etc.
            throw new RuntimeException("Error while obtaining Zoom OAuth token", e);
        }
    }
    
}
