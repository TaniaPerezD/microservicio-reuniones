package online.sis_ucb.meetings_service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map; // Para el campo settings, que a menudo es un mapa

// Solo incluir campos no nulos al serializar a JSON
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Meeting {

    private String topic;

    // Tipo de reunión: 2 para reunión agendada
    // Otros tipos: 1=Instant, 3=Recurring (no fixed time), 8=Recurring (fixed time)
    private Integer type;

    @JsonProperty("start_time")
    private String startTime; // Hora de inicio en formato ISO 8601 con zona horaria

    @JsonProperty("start_time_zoned")
    private ZonedDateTime startTimeZoned; // Hora de inicio como ZonedDateTime (opcional)

    private Integer duration; // Duración en minutos

    private String timezone; 

    private String password; // Contraseña de la reunión (opcional)

    private String agenda; // Agenda de la reunión (opcional)

    @JsonProperty("schedule_for")
    private String scheduleFor; // El ID de usuario o email del anfitrión de la reunión

    @JsonProperty("meeting_invitees")
    private List<MeetingInvitee> meetingInvitees; // Lista de invitados (opcional)

    private Map<String, Object> settings; // Configuraciones de la reunión (puede ser un mapa genérico o una clase dedicada)

    @JsonProperty("id")
    private Long id; // ID numérico de la reunión asignado por Zoom
    @JsonProperty("uuid")
    private String uuid; // UUID de la reunión
    @JsonProperty("join_url")
    private String joinUrl; // URL para que los participantes se unan
    @JsonProperty("start_url")
    private String startUrl; // URL para que el anfitrión inicie la reunión
    @JsonProperty("created_at")
    private ZonedDateTime createdAt; // Fecha y hora de creación de la reunión
    private String status; // Estado de la reunión


    // --- Constructor vacío (necesario para Jackson) ---
    public Meeting() {
    }

    // --- Constructor con campos comunes para crear una reunión agendada ---
    public Meeting(String topic, String startTime, Integer duration, String timezone, String scheduleFor) {
        this.topic = topic;
        this.type = 2; // Por defecto a agendada
        this.startTime = startTime;
        this.duration = duration;
        this.timezone = timezone;
        this.scheduleFor = scheduleFor;
    }

    // --- Getters y Setters (Generados automáticamente por tu IDE o añádelos manualmente) ---

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getScheduleFor() {
        return scheduleFor;
    }

    public void setScheduleFor(String scheduleFor) {
        this.scheduleFor = scheduleFor;
    }

    public List<MeetingInvitee> getMeetingInvitees() {
        return meetingInvitees;
    }

    public void setMeetingInvitees(List<MeetingInvitee> meetingInvitees) {
        this.meetingInvitees = meetingInvitees;
    }

    public Map<String, Object> getSettings() {
        return settings;
    }

    public void setSettings(Map<String, Object> settings) {
        this.settings = settings;
    }

    public ZonedDateTime getStartTimeZoned() {
        return startTimeZoned;
    }
    public void setStartTimeZoned(ZonedDateTime startTimeZoned) {
        this.startTimeZoned = startTimeZoned;
    }

    // --- Getters y Setters para campos de respuesta (si usas la misma clase) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getJoinUrl() {
        return joinUrl;
    }

    public void setJoinUrl(String joinUrl) {
        this.joinUrl = joinUrl;
    }

    public String getStartUrl() {
        return startUrl;
    }

    public void setStartUrl(String startUrl) {
        this.startUrl = startUrl;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}