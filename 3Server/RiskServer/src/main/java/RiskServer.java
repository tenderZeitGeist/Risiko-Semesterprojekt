import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by YEAH BOIIIIIIIIIIIIIII on 02.08.2017.
 */
public class RiskServer {

    public RiskServer() {
    }

    public static void main(String[] args) {
        try {
            RiskServer obj = new RiskServer();


            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();


            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

}
