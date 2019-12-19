package app.entities;

import java.util.Objects;

public class VisaStatus extends Entity {
    private long id;
    private long clientId;
    private VisaStatus status;

    public VisaStatus() {
        super();
    }

    public VisaStatus(long id, long clientId, VisaStatus status) {
        this.id = id;
        this.clientId = clientId;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public VisaStatus getStatus() {
        return status;
    }

    public void setStatus(VisaStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisaStatus that = (VisaStatus) o;
        return getId() == that.getId() &&
                getClientId() == that.getClientId() &&
                Objects.equals(getStatus(), that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), getStatus());
    }

    @Override
    public String toString() {
        return "VisaStatus{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", status='" + status + '\'' +
                '}';
    }
}