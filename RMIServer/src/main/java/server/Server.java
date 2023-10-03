package server;

import interfaces.RMIService;
import server.config.Config;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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
            System.out.println("Bound!");
            System.out.println("Server will wait forever for messages.");
            while (true) {
                try {
                    Thread.sleep(1000);
                    assert implementation != null;
                    if (implementation.isVotingOpen()) {
                        System.out.println("Voting is open.");
                    } else {
                        System.out.println("Voting is closed.");
                        System.out.println(implementation.requestResult().toPrettyString());
                        System.exit(0);
                    }
                } catch (InterruptedException e) {
                    exitWithException(e);
                }
            }
        } catch (RemoteException e) {
            exitWithException(e);
        }


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
            Config config = Config.fromFile("config.ser");
            var votingService = new VotingServiceImpl(config.getUsers().candidates());
            var authService = new AuthServiceImpl(config.getUsers());
            return new RMIServiceImpl(votingService, authService, config.getStopCondition());
        } catch (RemoteException | FileNotFoundException e) {
            exitWithException(e);
            return null;
        }

    }


}