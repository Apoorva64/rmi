package server.config;

import data.ID;
import data.Pitch;
import interfaces.Candidate;
import interfaces.User;
import server.AuthService;
import server.CandidateImpl;
import server.IO;
import server.UserImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Config implements java.io.Serializable {
    private final AuthService.UserList users;
    private final StartCondition startCondition;
    private final StopCondition stopCondition;

    Config(AuthService.UserList users, StartCondition startCondition, StopCondition stopCondition) {
        this.users = users;
        this.startCondition = startCondition;
        this.stopCondition = stopCondition;
    }

    public static Config getConfig(AuthService authService) throws RemoteException {
        var users = inputUsers();

        StopCondition stopCondition = switch (IO.choice("How do you want to stop the vote?", Arrays.asList("When all voters have voted", "When the administrator presses a key", "With a start and end date"), false)) {
            case 0 -> new StopWhenAllUsersHaveVoted(authService);
            case 1 -> new StopWithKey('c');
            default -> new StopWithDate(new Date(), new Date());
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
            if (isCandidate == 1) {
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
        if (isPitchAVideoOrText == 1) {
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
        // TODO: do not hardcode candidates
        // [Hard] Develop a Java application generating the necessary data structures
        // to represent the candidates to be used once the RMI server side is launched.
        // This application could be used by the administrator of the vote to have
        // him/her fill up the needed information and serialize the objects to obtain
        // a file of objects if it doesn't exist already. Then have the server
        // deserialize the file (assuming it knows where these files are located,
        // by default, on the same physical machine).
        Config config = new Config(List.of(new CandidateImpl(new ID("123457"), "password", "John Doe", new Pitch.TextPitch("I am John Doe")), new CandidateImpl(new ID("654321"), "password", "Jane Doe", new Pitch.TextPitch("I am Jane Doe")), new UserImpl(new ID("123456"), "password"), new UserImpl(new ID("654321"), "password")), startCondition, new StopWhenAllUsersHaveVoted()

        );
        Config.toFile(config, "config.ser");
        config = Config.fromFile("config.ser");

        config.getUsers().candidates().forEach(candidate -> {
            try {
                System.out.println(candidate.getName());
            } catch (RemoteException e) {
                System.err.println("Error reading from file");
            }
        });
        config.users.voters().forEach(user -> {
            try {
                System.out.println(user.getStudentNumber());
            } catch (RemoteException e) {
                System.err.println("Error reading from file");
            }
        });

        System.out.println(config.getStopCondition());
        System.exit(0);
    }

    public AuthService.UserList getUsers() {
        return users;
    }

    public StopCondition getStopCondition() {
        return stopCondition;
    }

}
