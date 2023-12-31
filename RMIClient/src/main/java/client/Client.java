package client;

import data.ID;
import data.IO;
import data.VoteValue;
import interfaces.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static data.Utils.exitWithException;
import static data.Utils.readLine;

public class Client {

    public static void main(String[] args) {
        try {
            var service = getRmiService();

            List<Candidate> candidates = service.listCandidates();

            System.out.println("Enter your student number: ");

            var studentNumber = new ID(readLine());
            var otp = getOtp(service, studentNumber);
            vote(candidates, service, studentNumber, otp);

            while (service.isVotingOpen()) {
                try {
                    System.out.println("Waiting for result...");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    exitWithException(e);
                }
            }

            System.out.println("Result: " + service.requestResult().toPrettyString());

            System.exit(0); // with RMI, we need to exit manually
        } catch (RemoteException | NotBoundException e) {
            exitWithException(e);
        }
    }

    private static void vote(List<Candidate> candidates, RMIService service, ID studentNumber, String otp) throws RemoteException {
        Map<ID, VoteValue> votes = getVotes(candidates);
        try {
            service.vote(votes, studentNumber, otp);
        } catch (HasAlreadyVotedException | VotingIsClosedException e) {
            System.out.println("You have already voted");
            System.exit(0);
        }
    }

    private static String getOtp(RMIService service, ID studentNumber) throws RemoteException {
        var passwordRequester = new ClientPasswordRequesterImpl();

        while (true) {
            try {
                return service.getVoteMaterial(studentNumber, passwordRequester);
            } catch (AuthenticationFailure e) {
                System.out.println("Wrong password, try again");
            }
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
        try {
            var possibleVoteValues = Arrays.stream(VoteValue.values()).map(VoteValue::toString).toList();

            System.out.println("Vote for " + candidate.getName() + " ?");
            candidate.getPitch().display();
            int choice = IO.choice("Vote: ", possibleVoteValues, false);
            return VoteValue.values()[choice];
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
