package server.config;

import server.AuthService;

import java.io.Serial;
import java.util.Objects;

public final class StopWhenAllUsersHaveVoted implements StopCondition {
    @Serial
    private static final long serialVersionUID = 0L;
    private AuthService authService;

    public StopWhenAllUsersHaveVoted() {
    }


    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean isReached() {
        return authService.getRemainingVoters() == 0;
    }

    public AuthService authService() {
        return authService;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (StopWhenAllUsersHaveVoted) obj;
        return Objects.equals(this.authService, that.authService);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authService);
    }

    @Override
    public String toString() {
        return "StopWhenAllUsersHaveVoted[" +
                "authService=" + authService + ']';
    }

}
