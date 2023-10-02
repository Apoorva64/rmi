package server;

import data.ID;
import interfaces.*;

import java.util.List;

public interface AuthService {
    String authentificate(ID username, ClientPasswordRequester passwordRequester) throws AuthenticationFailure;

    boolean validateOTP(ID studentNumber, String otp) throws HasAlreadyVotedException;

    record UserList(List<User> voters, List<Candidate> candidates) {
    }
}
