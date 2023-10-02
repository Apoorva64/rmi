package server;

import data.ID;
import data.Pitch;
import interfaces.Candidate;
import interfaces.RMIService;
import interfaces.User;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;

import static data.Utils.exitWithException;

public class Server {
    public static void main(String[] args) {
        // TODO: handle start
        // Set up the voting conditions: to do so, either the administrator
        // (who launched the server) can type (in the server's console)
        // the start and end dates, or the server can simply wait for a given key
        // to start the vote and another key to stop it, or the vote can keep
        // going until all users have voted, the choice is yours.


        try {
            RMIService implementation = getRmiService();

            // Export the object.
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(RMIService.RMI_NAME, implementation);
        } catch (RemoteException e) {
            exitWithException(e);
        }
        System.out.println("Bound!");
        System.out.println("Server will wait forever for messages.");

        // TODO: The results are printed at server side at the end

        // TODO: vote is over
        // When the vote is over (how/when it happens depends on the configuration at server start):
        //    - The score of each candidate is available (it may have been computed / modified each time
        //    the vote took place by adding the scores to each candidate).
        //    - The results are printed at server side
        //    - They can also be retrieved at client side if needed
    }

    private static RMIServiceImpl getRmiService() {
        try {
            var users = new AuthService.UserList(getVoters(), getCandidates());
            var votingService = new VotingServiceImpl(users.candidates());
            var authService = new AuthServiceImpl(users);
            return new RMIServiceImpl(votingService, authService);
        } catch (RemoteException e) {
            exitWithException(e);
            return null;
        }

    }

    public static List<Candidate> getCandidates() {
        try {
            return List.of(
                    new CandidateImpl(new ID("123456"), "password", "John Doe", new Pitch.TextPitch("I am John Doe")),
                    new CandidateImpl(new ID("654321"), "password", "Jane Doe", new Pitch.TextPitch("I am Jane Doe"))
            );
        } catch (Exception e) {
            exitWithException(e);
            return null;
        }

        // TODO: do not hardcode candidates
        // [Hard] Develop a Java application generating the necessary data structures
        // to represent the candidates to be used once the RMI server side is launched.
        // This application could be used by the administrator of the vote to have
        // him/her fill up the needed information and serialize the objects to obtain
        // a file of objects if it doesn't exist already. Then have the server
        // deserialize the file (assuming it knows where these files are located,
        // by default, on the same physical machine).
    }

    public static List<User> getVoters() {
        try {
            return List.of(
                    new UserImpl(new ID("123456"), "password")

            );
        } catch (Exception e) {
            exitWithException(e);
            return null;
        }
    }
}
