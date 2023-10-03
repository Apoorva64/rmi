package server.config;

import server.AuthService;

public final class StopWhenAllUsersHaveVoted implements StopCondition {
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
}
