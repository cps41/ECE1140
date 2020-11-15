/* Needed on both Server and Client */

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote {
    int updateInterval = 100;

    void serverSend(String key, Object value) throws RemoteException;

    void serverSend(String key, Object value, boolean burnAfterReading) throws RemoteException;

    Object serverReceive(String key) throws RemoteException;

    void serverCall(String key, Object... argv) throws RemoteException;

    Object[] serverGetCall(String key) throws RemoteException;
}
