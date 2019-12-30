package app.entities;

import java.util.Objects;

public class VisaStatus extends Entity {
    private long id;
    private User client;
    private VisaStatus status;

    public VisaStatus() {
        super();
    }

    public VisaStatus(User client, VisaStatus status) {
        this.client = client;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
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
                Objects.equals(getClient(), that.getClient()) &&
                Objects.equals(getStatus(), that.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClient(), getStatus());
    }

    @Override
    public String toString() {
        return "VisaStatus{" +
                "id=" + id +
                ", client=" + client +
                ", status='" + status + '\'' +
                '}';
    }
}