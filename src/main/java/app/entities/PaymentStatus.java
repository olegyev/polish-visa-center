package app.entities;

import java.util.Objects;

public class PaymentStatus extends Entity {
    private long id;
    private User client;
    private boolean visaFee;
    private boolean serviceFee;

    public PaymentStatus() {
        super();
    }

    public PaymentStatus(User client, boolean visaFee, boolean serviceFee) {
        this.client = client;
        this.visaFee = visaFee;
        this.serviceFee = serviceFee;
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

    public boolean isVisaFee() {
        return visaFee;
    }

    public void setVisaFee(boolean visaFee) {
        this.visaFee = visaFee;
    }

    public boolean isServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(boolean serviceFee) {
        this.serviceFee = serviceFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentStatus that = (PaymentStatus) o;
        return getId() == that.getId() &&
                isVisaFee() == that.isVisaFee() &&
                isServiceFee() == that.isServiceFee() &&
                Objects.equals(getClient(), that.getClient());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClient(), isVisaFee(), isServiceFee());
    }

    @Override
    public String toString() {
        return "PaymentStatus{" +
                "id=" + id +
                ", client=" + client +
                ", visaFee=" + visaFee +
                ", serviceFee=" + serviceFee +
                '}';
    }
}