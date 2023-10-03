package server;

import data.ID;
import data.VoteValue;
import interfaces.*;
import server.config.StopCondition;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;

public class RMIServiceImpl extends UnicastRemoteObject implements RMIService {
    VotingService votingService;
    AuthService authService;
    StopCondition stopCondition;

    public RMIServiceImpl(VotingService votingService, AuthService authService, StopCondition stopCondition) throws RemoteException {
        super();
        this.votingService = votingService;
        this.authService = authService;
        this.stopCondition = stopCondition;
    }

    @Override
    public List<Candidate> listCandidates() throws RemoteException {
        return votingService.getCandidates();
    }

    @Override
    public String getVoteMaterial(ID studentNumber, ClientPasswordRequester passwordRequester) throws RemoteException, AuthenticationFailure {
        return authService.authentificate(studentNumber, passwordRequester);
    }

    @Override
    public void vote(Map<ID, VoteValue> vote, ID studentNumber, String oneTimePassword) throws RemoteException, HasAlreadyVotedException, VotingIsClosedException {
        if (!isVotingOpen()) {
            throw new VotingIsClosedException();
        }
        if (authService.validateOTP(studentNumber, oneTimePassword)) {
            votingService.vote(vote);
        }
        System.out.println(studentNumber + " has voted.");
        System.out.println("Remaining voters: " + authService.getRemainingVoters());

        if (authService.getRemainingVoters() == 0) {
            System.out.println(votingService.requestResult().toPrettyString());
            System.out.println("All voters have voted. Shutting down.");
            System.exit(0);
        }
    }

    @Override
    public VoteResult requestResult() throws RemoteException {
        return votingService.requestResult();
    }

    public boolean isVotingOpen() throws RemoteException {
        return !stopCondition.isReached();
    }

}
