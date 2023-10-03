package server;

import data.IO;
import interfaces.RMIService;
import server.config.Config;

import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Objects;

import static data.Utils.exitWithException;

public class Server {
    public static void main(String[] args) {
        try {
            RMIService implementation = Objects.requireNonNull(getRmiService());

            // Export the object.
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind(RMIService.RMI_NAME, implementation);
            System.out.println("Bound!");
            System.out.println("Server will wait forever for messages.");

            // wait for start
            while (!implementation.isVotingOpen()) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    exitWithException(e);
                }
            }

            System.out.println("Voting is open.");

            // exit at the end
            while (implementation.isVotingOpen()) {
                try {
                    Thread.sleep(3000); // avoid busy waiting
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            System.out.println("Voting over");
            System.out.println(implementation.requestResult().toPrettyString());


        } catch (RemoteException e) {
            exitWithException(e);
        }
    }


    private static RMIServiceImpl getRmiService() {
        try {
            Config config = Config.fromFile(IO.readLine("Enter config file name: "));
            var votingService = new VotingServiceImpl(config.getUsers().candidates());
            var authService = new AuthServiceImpl(config.getUsers());
            return new RMIServiceImpl(votingService, authService, config.getStartCondition(), config.getStopCondition());
        } catch (RemoteException | FileNotFoundException e) {
            exitWithException(e);
            return null;
        }

    }
}