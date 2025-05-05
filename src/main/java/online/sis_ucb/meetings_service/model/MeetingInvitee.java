package online.sis_ucb.meetings_service.model;

public class MeetingInvitee {
    private String email;

    // Puedes añadir otros campos si la documentación de Zoom lo indica para invitados (ej: name)
    // private String name;

    // --- Constructor vacío (necesario para Jackson) ---
    public MeetingInvitee() {
    }

    // --- Constructor con email ---
    public MeetingInvitee(String email) {
        this.email = email;
    }

    // --- Getters y Setters ---

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
