package app.entities;

import java.util.Objects;

public class PaymentStatus extends Entity {
    private long id;
    private long clientId;
    private boolean visaFee;
    private boolean serviceFee;

    public PaymentStatus() {
        super();
    }

    public PaymentStatus(long id, long clientId, boolean visaFee, boolean serviceFee) {
        this.id = id;
        this.clientId = clientId;
        this.visaFee = visaFee;
        this.serviceFee = serviceFee;
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
                getClientId() == that.getClientId() &&
                isVisaFee() == that.isVisaFee() &&
                isServiceFee() == that.isServiceFee();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getClientId(), isVisaFee(), isServiceFee());
    }

    @Override
    public String toString() {
        return "PaymentStatus{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", visaFee=" + visaFee +
                ", serviceFee=" + serviceFee +
                '}';
    }
}