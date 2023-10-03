package server.config;

import server.AuthService;

public record StopWhenAllUsersHaveVoted(AuthService authService) implements StopCondition {
    @Override
    public boolean isReached() {
        return authService.getRemainingVoters() == 0;
    }
}
