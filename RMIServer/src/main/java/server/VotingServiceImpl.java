package server;

import data.ID;
import data.VoteValue;
import interfaces.Candidate;
import interfaces.VoteResult;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static data.Utils.exitWithException;

public class VotingServiceImpl implements VotingService {
    Map<ID, Integer> voteValues = new ConcurrentHashMap<>();

    VotingServiceImpl(List<Candidate> candidates) {
        try {
            for (Candidate candidate : candidates) {
                voteValues.put(candidate.getStudentNumber(), 0);
            }
        } catch (RemoteException e) {
            exitWithException(e);
        }
    }

    @Override
    public List<Candidate> getCandidates() {
        return AuthServiceImpl.getUsers().candidates();
    }

    @Override
    public void vote(Map<ID, VoteValue> vote) {
        for (var entry : vote.entrySet()) {
            voteValues.computeIfPresent(entry.getKey(), (candidate, integer) -> integer + entry.getValue().value());
        }
    }

    @Override
    public VoteResult requestResult() {
        try {
            return new VoteResultImpl(voteValues);
        } catch (RemoteException e) {
            exitWithException(e);
            return null;
        }
    }
}
