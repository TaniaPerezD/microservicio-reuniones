package online.sis_ucb.meetings_service.controller;


import online.sis_ucb.meetings_service.model.Meeting;
import online.sis_ucb.meetings_service.model.MeetingListResponse;
import online.sis_ucb.meetings_service.service.ZoomMeetingService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/meetings")

public class MeetingController {
    private final ZoomMeetingService zoomMeetingService;
    private static final Logger logger = LoggerFactory.getLogger(MeetingController.class);

    public MeetingController(ZoomMeetingService zoomMeetingService) {
        this.zoomMeetingService = zoomMeetingService;
    }

    @PostMapping("/create/{userId}")
    public ResponseEntity<Meeting> createMeeting(@PathVariable String userId, @RequestBody Meeting meeting) {
        logger.info("Received request to create meeting for user: {}", userId);
        
        try {
            //parse the start_time(string with format: yyyy-mm-dd hh:mm:ssTZ) to ZonedDateTime
            meeting.setStartTimeZoned(ZonedDateTime.parse(meeting.getStartTime()));

            // Validar que la reunión tenga una fecha de inicio programada
            if (meeting.getStartTimeZoned() == null) {
                logger.warn("Start time is missing, setting default to future time");
                // Si no se proporciona startTime, establecer por defecto a 1 hora en el futuro
                meeting.setStartTimeZoned(ZonedDateTime.now().plusHours(1));
            }
            
            // Validar que la fecha es futura
            if (meeting.getStartTimeZoned().isBefore(ZonedDateTime.now())) {
                logger.warn("Start time is in the past: {}, adjusting to future time", meeting.getStartTime());
                // Si la fecha es en el pasado, ajustar a 1 hora en el futuro
                meeting.setStartTimeZoned(ZonedDateTime.now().plusHours(1));
            }
            
            // Asegúrate de que el tipo es para reunión agendada (tipo 2)
            if (meeting.getType() == null) {
                meeting.setType(2); // Tipo 2 = Reunión agendada
            }
            
            logger.info("Creating scheduled meeting with start time: {}", meeting.getStartTime());
            Meeting createdMeeting = zoomMeetingService.createMeeting(userId, meeting);
            return new ResponseEntity<>(createdMeeting, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            logger.error("Error during meeting creation for user {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<MeetingListResponse> listMeetings(@PathVariable String userId) {
        try {
            MeetingListResponse meetings = zoomMeetingService.listMeetings(userId);
            return new ResponseEntity<>(meetings, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{meetingId}")
     public ResponseEntity<Meeting> getMeeting(@PathVariable Long meetingId) {
        try {
            Meeting meeting = zoomMeetingService.getMeeting(meetingId);
            if (meeting != null) {
                return new ResponseEntity<>(meeting, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
     }


    @PatchMapping("/update/{meetingId}")
    public ResponseEntity<Void> updateMeeting(@PathVariable Long meetingId, @RequestBody Meeting meetingUpdates) {
        try {
            logger.info("Received request to update meeting with ID: {}", meetingId);
            logger.info("Meeting start_tiem: {}", meetingUpdates.getStartTime());
            zoomMeetingService.updateMeeting(meetingId, meetingUpdates);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            logger.error("Error during meeting update for meeting ID {}", meetingId, e); // <-- Loggea la excepción completa
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{meetingId}")
     public ResponseEntity<Void> deleteMeeting(@PathVariable Long meetingId) {
        try {
            zoomMeetingService.deleteMeeting(meetingId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
     }
    
}
