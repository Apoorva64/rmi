package server;

import data.ID;
import interfaces.Candidate;
import interfaces.VoteResult;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VoteResultImpl extends UnicastRemoteObject implements VoteResult {
    private final Map<ID, Integer> result;

    public VoteResultImpl(Map<ID, Integer> result) throws RemoteException {
        super();
        this.result = result;
    }

    @Override
    public String toPrettyString() throws RemoteException {
        var candidates = AuthServiceImpl.getUsers().candidates();
        StringBuilder builder = new StringBuilder();
        for (var candidate : candidates) {
            builder.append(candidate.getName()).append(": ").append(result.get(candidate.getStudentNumber())).append("\n");
        }

        builder.append("Winner: ").append(getWinner(candidates).getName()).append("\n");
        return builder.toString();
    }

    private Candidate getWinner(List<Candidate> candidates) throws RemoteException {
        // I'd like to use streams here, but RemoteExceptions make it ugly
        int max = 0;
        Candidate winner = null;
        for (var candidate : candidates) {
            if (result.get(candidate.getStudentNumber()) > max) {
                max = result.get(candidate.getStudentNumber());
                winner = candidate;
            }
        }
        return Objects.requireNonNull(winner);
    }
}
