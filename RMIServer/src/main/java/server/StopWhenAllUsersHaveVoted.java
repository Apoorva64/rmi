package server;

public class StopWhenAllUsersHaveVoted implements StopCondition {
    transient private int remainingUsers = 0;

    public void setRemainingUsers(int remainingUsers) {
        this.remainingUsers = remainingUsers;
    }

    @Override
    public boolean isReached() {
        return remainingUsers == 0;
    }
}
