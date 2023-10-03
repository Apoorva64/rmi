package server;

import data.ID;
import interfaces.AuthenticationFailure;
import interfaces.ClientPasswordRequester;
import interfaces.HasAlreadyVotedException;
import interfaces.User;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static data.Utils.exitWithException;

/**
 * A simple implementation of a printing interface.
 */
public class AuthServiceImpl implements AuthService {
    private static UserList users;
    Map<ID, OTP> otps = new ConcurrentHashMap<>();

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
            exitWithException(e);
        }
        throw new AuthenticationFailure();
    }

    private String generateOTP(ID studentNumber) {
        return otps.computeIfAbsent(studentNumber, k -> new OTP()).get();
    }

    public boolean validateOTP(ID studentNumber, String otp) throws HasAlreadyVotedException {
        if (this.otps.containsKey(studentNumber) && this.otps.get(studentNumber).isValid()) {
            this.otps.get(studentNumber).invalidate();
            return this.otps.get(studentNumber).get().equals(otp);
        }
        throw new HasAlreadyVotedException();
    }

    public int getRemainingVoters() {
        return (int) (users.voters().size() - otps.entrySet().stream().filter(e -> !e.getValue().isValid()).count());
    }
}
