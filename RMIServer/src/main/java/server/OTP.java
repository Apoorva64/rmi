package server;

// TODO :
// Bonus: you can extend the way the one-time password works so that the user
// can restart his/her vote. In this case, you need to ask for this
// OTP if a user tries to retrieve the voting material multiple times
// (as he needs yet another OTP). Care must also be taken to ensure the cancelation
// the previous vote done by the same voter if your system really
// allows to vote several times before the closing vote date.

public class OTP {
    private final String otp;
    boolean valid = true;

    public OTP() {
        this.otp = String.valueOf((int) (Math.random() * 1000000));
    }

    public String get() {
        return otp;
    }

    void invalidate() {
        valid = false;
    }

    public boolean isValid() {
        return valid;
    }
}
