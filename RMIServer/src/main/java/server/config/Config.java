package server.config;

import data.ID;
import data.IO;
import data.Pitch;
import interfaces.User;
import server.AuthService;
import server.CandidateImpl;
import server.UserImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static data.Utils.exitWithException;

public class Config implements java.io.Serializable {
    private final AuthService.UserList users;
    private final StartCondition startCondition;
    private final StopCondition stopCondition;

    Config(AuthService.UserList users, StartCondition startCondition, StopCondition stopCondition) {
        this.users = users;
        this.startCondition = startCondition;
        this.stopCondition = stopCondition;
    }

    public static Config inputConfig() throws RemoteException {
        var users = inputUsers();

        StopCondition stopCondition = switch (IO.choice("How do you want to stop the vote?", Arrays.asList("When all voters have voted", "When the administrator presses a key", "With a start and end date"), false)) {
            case 0 -> new StopWhenAllUsersHaveVoted();
            case 1 -> new StopWithKey();
            default -> new StopWithDate(new Date());
        };

        if (stopCondition instanceof StopWithDate) {
            Date startDate = IO.readDate("Enter the start date of the vote");
            Date endDate = IO.readDate("Enter the end date of the vote");
            return new Config(users, new StartWithDate(startDate), new StopWithDate(endDate));
        } else {
            return new Config(users, new StartWithoutCondition(), stopCondition);
        }
    }

    public static AuthService.UserList inputUsers() throws RemoteException {
        int numberOfVoters = IO.readInt("How many Users are there?");
        User[] users = new User[numberOfVoters];

        for (int i = 0; i < numberOfVoters; i++) {
            String studentNumber = IO.readLine("Enter the student number of user " + (i + 1));
            String password = IO.readLine("Enter the password of user " + (i + 1));
            int isCandidate = IO.choice("Is user " + (i + 1) + " a candidate?", Arrays.asList("Yes", "No"), false);
            if (isCandidate == 0) {
                inputCandidate(users, i, studentNumber, password);
            } else {
                users[i] = new UserImpl(new ID(studentNumber), password);
            }
        }

        return new AuthService.UserList(Arrays.asList(users));
    }

    private static void inputCandidate(User[] users, int i, String studentNumber, String password) throws RemoteException {
        String candidateName = IO.readLine("Enter the name of the candidate");
        int isPitchAVideoOrText = IO.choice("Is the pitch a video or text?", Arrays.asList("Video", "Text"), false);
        if (isPitchAVideoOrText == 0) {
            String candidatePitch = IO.readLine("Enter the Video URL of the candidate");
            users[i] = new CandidateImpl(new ID(studentNumber), password, candidateName, new Pitch.VideoPitch(candidatePitch));
        } else {
            String candidatePitch = IO.readLine("Enter the Text pitch of the candidate");
            users[i] = new CandidateImpl(new ID(studentNumber), password, candidateName, new Pitch.TextPitch(candidatePitch));
        }
    }


    public static void toFile(Config config, String fileName) throws FileNotFoundException {
        FileOutputStream fileOut = new FileOutputStream(fileName);
        try {
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(fileOut);
            out.writeObject(config);
            out.close();
            fileOut.close();
        } catch (java.io.IOException e) {
            System.err.println("Error writing to file");
            System.exit(1);
        }

    }

    public static Config fromFile(String fileName) throws FileNotFoundException {
        // pwd
        try {
            ID.ids.clear();
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.FileInputStream(fileName));
            Config config = (Config) in.readObject();
            in.close();
            return config;
        } catch (java.io.IOException | ClassNotFoundException e) {
            System.err.println("Error reading from file");
            System.exit(1);
            return null;
        }
    }

    public static void main(String[] args) throws RemoteException, FileNotFoundException {
        Config config = new Config(new AuthService.UserList(
                List.of(
                        new CandidateImpl(
                                new ID("123457"), "password", "John Doe",
                                new Pitch.TextPitch("I am John Doe")),
                        new CandidateImpl(new ID("654321"), "password", "Jane Doe",
                                new Pitch.TextPitch("I am Jane Doe"))
                )
        ),
                new StartWithoutCondition(), new StopWhenAllUsersHaveVoted());

        Config.toFile(config, "config.ser");
        System.out.println("Default config written to config.ser");

        System.out.println("Users: ");
        config.users.voters().forEach(user -> {
            try {
                System.out.println(user.getStudentNumber());
            } catch (RemoteException e) {
                exitWithException(e);
            }
        });
        System.out.println("Start condition: ");
        System.out.println(config.getStartCondition());
        System.out.println("End condition:");
        System.out.println(config.getStopCondition());

        if (IO.choice(
                "Do you want to write a custom config",
                List.of("yes", "no"),
                false
        ) == 1) {
            System.exit(0);
        }

        Config customConfig = Config.inputConfig();

        String fileName = IO.readLine("Enter the name of the file to write the config to");
        Config.toFile(customConfig, fileName);

    }

    public AuthService.UserList getUsers() {
        return users;
    }

    public StopCondition getStopCondition() {
        return stopCondition;
    }

    public StartCondition getStartCondition() {
        return startCondition;
    }
}
