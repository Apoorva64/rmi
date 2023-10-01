package client;

import data.ID;
import data.VoteValue;
import interfaces.Candidate;
import interfaces.RMIService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static data.Utils.readLine;

public class Client {

    public static void main(String[] args) {
        try {
            var service = getRmiService();

            List<Candidate> candidates = service.listCandidates();

            System.out.println("Enter your student number: ");
            var studentNumber = new ID(readLine());

            var passwordRequester = new ClientPasswordRequesterImpl();
            var otp = service.getVoteMaterial(studentNumber, passwordRequester);

            Map<ID, VoteValue> votes = getVotes(candidates);
            service.vote(votes, studentNumber, otp);

            System.out.println("Result: " + service.requestResult().toPrettyString());

            System.exit(0); // with RMI, we need to exit manually
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static RMIService getRmiService() throws RemoteException, NotBoundException {
        var registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        return (RMIService) registry.lookup(RMIService.RMI_NAME);
    }

    private static Map<ID, VoteValue> getVotes(List<Candidate> candidates) throws RemoteException {
        var res = new HashMap<ID, VoteValue>();
        for (var candidate : candidates) {
            var vote = getVote(candidate);
            res.put(candidate.getStudentNumber(), vote);
        }
        return res;
    }

    private static VoteValue getVote(Candidate candidate) {
        Arrays.stream(VoteValue.values()).map(
                vote -> vote.value() + ": " + vote
        ).forEach(System.out::println);
        int vote = 0;
        try {
            System.out.println("Candidate: " + candidate.getName());
            candidate.getPitch().display();

            System.out.println("Enter your vote: ");
            vote = Integer.parseInt(readLine());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return VoteValue.fromValue(vote);
    }
}
