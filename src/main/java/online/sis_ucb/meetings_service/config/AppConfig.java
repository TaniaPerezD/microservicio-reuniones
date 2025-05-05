package online.sis_ucb.meetings_service.config;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory; // Importa esta clase
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Crea un cliente HTTP de Apache
        HttpClient httpClient = HttpClients.createDefault();

        // Usa HttpComponentsClientHttpRequestFactory para envolver el cliente Apache
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

        // Configura RestTemplate para usar esta nueva fábrica
        return new RestTemplate(requestFactory);
    }

    // ... otros beans si los tienes ...
}