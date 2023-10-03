package interfaces;

import data.ID;
import data.VoteValue;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface RMIService extends Remote, Serializable {
    String RMI_NAME = "Service";

    List<Candidate> listCandidates() throws RemoteException;

    /// @return One-time password
    String getVoteMaterial(ID studentNumber, ClientPasswordRequester passwordRequester) throws RemoteException, AuthenticationFailure;

    // TODO: Bonus: for further logging and checks on how the whole voting process ran,
    //  you can store on the server additional information alongside the student number
    //  (date of the vote, name of the voter (by calling back a
    //  method on the client stub), ...).

    void vote(Map<ID, VoteValue> vote, ID studentNumber, String oneTimePassword) throws RemoteException, HasAlreadyVotedException, VotingIsClosedException;

    VoteResult requestResult() throws RemoteException;

}
