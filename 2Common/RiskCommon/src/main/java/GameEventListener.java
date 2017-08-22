
import events.GameEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameEventListener extends Remote {

    void handleGameEvent(GameEvent event) throws RemoteException;
}
