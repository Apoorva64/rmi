package server;

import data.ID;
import interfaces.AuthenticationFailure;
import interfaces.ClientPasswordRequester;
import interfaces.InvalidVoteCredentials;
import interfaces.User;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of a printing interface.
 */
public class AuthServiceImpl implements AuthService {
    private static UserList users;
    Map<ID, OTP> otps = new HashMap<>();

    protected AuthServiceImpl(UserList users) throws RemoteException {
        super();
        AuthServiceImpl.users = users;
    }

    public static UserList getUsers() {
        return users;
    }

    @Override
    public String authentificate(ID studentNumber, ClientPasswordRequester passwordRequester) throws AuthenticationFailure {
        try {
            String password = passwordRequester.requestPassword();
            for (User user : users.voters()) {
                if (user.getStudentNumber().equals(studentNumber) && user.checkPassword(password)) {
                    return generateOTP(studentNumber);
                }
            }
        } catch (RemoteException e) {
            // TODO
        }
        throw new AuthenticationFailure();
    }

    private String generateOTP(ID studentNumber) {
        return otps.computeIfAbsent(studentNumber, k -> new OTP()).get();
    }

    public boolean validateOTP(ID studentNumber, String otp) throws InvalidVoteCredentials {
        if (this.otps.containsKey(studentNumber) && this.otps.get(studentNumber).isValid()) {
            this.otps.get(studentNumber).invalidate();
            return this.otps.get(studentNumber).get().equals(otp);
        }
        throw new InvalidVoteCredentials();
    }
}
