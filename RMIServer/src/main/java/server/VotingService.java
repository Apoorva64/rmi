package server;

import data.ID;
import data.VoteValue;
import interfaces.Candidate;
import interfaces.VoteResult;

import java.util.List;
import java.util.Map;

public interface VotingService {
    List<Candidate> getCandidates();

    void vote(Map<ID, VoteValue> vote);

    VoteResult requestResult();
}
