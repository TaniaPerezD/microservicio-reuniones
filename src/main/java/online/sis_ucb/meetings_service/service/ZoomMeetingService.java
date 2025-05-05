package online.sis_ucb.meetings_service.service;


import online.sis_ucb.meetings_service.model.Meeting;
import online.sis_ucb.meetings_service.model.MeetingListResponse;
import online.sis_ucb.meetings_service.oauth.ZoomOAuthService;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;



@Service
public class ZoomMeetingService {
    @Value("${zoom.api.base-url}")
    private String zoomApiBaseUrl;

    private final ZoomOAuthService zoomOAuthService;
    private final RestTemplate restTemplate; // Inyectar RestTemplate

    public ZoomMeetingService(ZoomOAuthService zoomOAuthService, RestTemplate restTemplate) {
        this.zoomOAuthService = zoomOAuthService;
        this.restTemplate = restTemplate;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(zoomOAuthService.getAccessToken());
        return headers;
    }

    // Crear reunión
    public Meeting createMeeting(String userId, Meeting meeting) {
        String url = zoomApiBaseUrl + "/users/" + userId + "/meetings";
        HttpHeaders headers = createHeaders();
        
        // Asegúrate de que el meeting tenga el tipo 2 (reunión agendada)
        if (meeting.getType() == null) {
            meeting.setType(2);
        }
        
        // // para depuración - ver el valor de startTime antes de enviarlo
        if (meeting.getStartTime() != null) {
            //ger.info("StartTime antes de enviar a Zoom: {}", meeting.getStartTime());
            
            // Asegúrate de que la hora esté en formato ISO
            String formattedTime = meeting.getStartTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            //ger.info("StartTime formateado: {}", formattedTime);
        } else {
            //ger.warn("StartTime es null, la reunión se creará para la hora actual");
        }

        HttpEntity<Meeting> request = new HttpEntity<>(meeting, headers);

        try {
            // // para depuración - ver el cuerpo de la solicitud
            //ger.info("Enviando solicitud a Zoom: {}", request);
            
            ResponseEntity<Meeting> response = restTemplate.postForEntity(url, request, Meeting.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                Meeting createdMeeting = response.getBody();
                
                // // para depuración - ver la respuesta
                if (createdMeeting != null && createdMeeting.getStartTime() != null) {
                    //ger.info("StartTime en la respuesta de Zoom: {}", createdMeeting.getStartTime());
                }
                
                return createdMeeting;
            } else {
                throw new RuntimeException("Failed to create Zoom meeting: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
            //ger.error("Zoom API error during meeting creation: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Zoom API error during meeting creation", e);
        } catch (Exception e) {
            //ger.error("Error during meeting creation", e);
            throw new RuntimeException("Error during meeting creation", e);
        }
    }

    // Listar reuniones (puede requerir paginación)
    public MeetingListResponse listMeetings(String userId) {
        String url = zoomApiBaseUrl + "/users/" + userId + "/meetings";
        HttpHeaders headers = createHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<MeetingListResponse> response = restTemplate.exchange(url, HttpMethod.GET, request, MeetingListResponse.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to list Zoom meetings: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
             System.err.println("Zoom API error during listing meetings: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Zoom API error during listing meetings", e);
        } catch (Exception e) {
            throw new RuntimeException("Error during listing meetings", e);
        }
    }

    // Obtener detalles de una reunión específica (útil antes de editar)
     public Meeting getMeeting(Long meetingId) {
        String url = zoomApiBaseUrl + "/meetings/" + meetingId;
        HttpHeaders headers = createHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Meeting> response = restTemplate.exchange(url, HttpMethod.GET, request, Meeting.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                return null; // O lanzar una excepción MeetingNotFoundException
            }
            else {
                 throw new RuntimeException("Failed to get Zoom meeting: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
             System.err.println("Zoom API error during getting meeting: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Zoom API error during getting meeting", e);
        } catch (Exception e) {
            throw new RuntimeException("Error during getting meeting", e);
        }
     }


    // Actualizar reunión
    public void updateMeeting(Long meetingId, Meeting meetingUpdates) {
        String url = zoomApiBaseUrl + "/meetings/" + meetingId;
        HttpHeaders headers = createHeaders();
        HttpEntity<Meeting> request = new HttpEntity<>(meetingUpdates, headers);

        try {
            // PATCH method requires exchange
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PATCH, request, Void.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to update Zoom meeting: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
             System.err.println("Zoom API error during meeting update: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Zoom API error during meeting update", e);
        } catch (Exception e) {
            throw new RuntimeException("Error during meeting update", e);
        }
    }

     // Eliminar reunión (opcional, pero útil)
    public void deleteMeeting(Long meetingId) {
        String url = zoomApiBaseUrl + "/meetings/" + meetingId;
        HttpHeaders headers = createHeaders();
        HttpEntity<?> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.DELETE, request, Void.class);

             if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Failed to delete Zoom meeting: " + response.getStatusCode());
            }
        } catch (HttpClientErrorException e) {
             System.err.println("Zoom API error during meeting deletion: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
            throw new RuntimeException("Zoom API error during meeting deletion", e);
        } catch (Exception e) {
            throw new RuntimeException("Error during meeting deletion", e);
        }
    }
    
}
