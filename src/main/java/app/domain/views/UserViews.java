package app.domain.views;

public final class UserViews {

    public interface AllExceptIdAndPassword {}

    public interface IncludingPassword extends AllExceptIdAndPassword {}

}