package interfaces;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VoteResult extends Serializable, Remote {
    String toPrettyString() throws RemoteException;
}
