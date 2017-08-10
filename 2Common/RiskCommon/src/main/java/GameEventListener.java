import com.sun.org.apache.regexp.internal.RE;
import events.GameEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 10.08.2017.
 */
public interface GameEventListener extends Remote {

    public void handleGameEvent(GameEvent event) throws RemoteException;
}
