package interfaces;


import data.Pitch;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Candidate extends User, Serializable {
    String getName() throws RemoteException;

    Pitch getPitch() throws RemoteException;
    
}
