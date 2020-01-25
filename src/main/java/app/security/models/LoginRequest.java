package app.security.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class LoginRequest implements Serializable {

    @NonNull
    private String username;

    @NonNull
    private String password;

}