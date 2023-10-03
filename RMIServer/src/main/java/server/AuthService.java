package server;

import data.ID;
import interfaces.*;

import java.io.Serializable;
import java.util.List;

public interface AuthService {
    String authentificate(ID username, ClientPasswordRequester passwordRequester) throws AuthenticationFailure;

    boolean validateOTP(ID studentNumber, String otp) throws HasAlreadyVotedException;

    int getRemainingVoters();

    record UserList(List<User> voters, List<Candidate> candidates) implements Serializable {
        public UserList(List<User> allUsers) {
            this(
                    allUsers,
                    allUsers.stream().filter(u -> u instanceof Candidate).map(u -> (Candidate) u).toList()
            );
        }
    }
}
