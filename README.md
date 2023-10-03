# Java RMI Report - Edgar and Appadoo Apoorva

## Choices Made
- We decided to leverage modern Java 17 along with its new features to ensure that our project is built on the latest technologies and benefits from the language improvements.
- To enhance code organization and maintainability, we opted to package shared code into a Maven package. This approach reduces the chances of errors that can occur when manually copying and pasting files.
- Emphasizing code sharing between the client and server components was a deliberate choice. This approach streamlines development and ensures consistency throughout the application.

## Implemented Optional Features
In addition to the core functionality, we have implemented optional features to enrich the user experience and provide more versatility:
- **Candidate Pitches**: We extended the system to allow candidates to provide pitches in either text or video format (via a link). This feature enhances the platform's interactivity and engagement.
- **Server Configuration**: We implemented a configuration tool that allows users to specify various settings when launching the server. These configurations include defining the list of candidates, user details, start time, and end conditions. End conditions can be based on a specific time, a key press, or when all users have cast their votes.

## Difficulties Encountered
During the development process, we encountered several challenges and obstacles:
- **Java RMI Complexity**: Java RMI (Remote Method Invocation) is an older technology that is less commonly used in modern applications. Consequently, finding up-to-date and relevant resources for guidance was challenging.
- **Obscure Error Messages**: Java RMI often provided error messages that were not very informative, making it difficult to pinpoint the root cause of issues. In some instances, we had to extend classes like `UnicastRemoteObject` without a clear understanding of why it was necessary.
- **Compile-time Issues**: We believe that some of the issues we encountered should ideally be identified at compile-time rather than runtime. Examples include missing `throws RemoteException` specifications and a lack of `Serializable` implementations. Improving compile-time error reporting in Java RMI could greatly enhance the development experience.

## How to Use
To use our Java RMI application, follow these steps:

1. **Configuration Tool**: To configure the server settings, run the following command:
   ```
   mvn compile exec:java -pl RMIServer -Dexec.mainClass="server.config.Config"
   ```

2. **Server**: Start the server using the following command:
   ```
   mvn compile exec:java -pl RMIServer -Dexec.mainClass="server.Server"
   ```
   'config.ser' can be used as the configuration file. There are 2 candidates in the file: 123457, 654321. The password for both is 'password'.

3. **Clients**: Launch clients using the following command:
   ```
   mvn compile exec:java -pl RMIClient -Dexec.mainClass="client.Client"
   ```

These commands will initiate the respective components of the Java RMI application, enabling you to configure, run, and interact with the system.

This report summarizes the choices we made, the optional features we implemented, the difficulties we encountered, and instructions on how to use our Java RMI application.
