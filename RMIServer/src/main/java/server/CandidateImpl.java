package server;

import data.ID;
import data.Pitch;
import interfaces.Candidate;

import java.io.Serializable;
import java.rmi.RemoteException;

public class CandidateImpl extends UserImpl implements Candidate, Serializable {
    String name;
    Pitch pitch;

    public CandidateImpl(ID id, String password, String name, Pitch pitch) throws RemoteException {
        super(id, password);

        this.name = name;
        this.pitch = pitch;
    }

    @Override
    public int hashCode() {
        return studentNumber.hashCode();
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public Pitch getPitch() throws RemoteException {
        return pitch;
    }
}
