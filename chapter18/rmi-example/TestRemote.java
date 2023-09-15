import java.rmi.*;

interface TestRemote extends Remote {
  public String test(String input) throws RemoteException;

  public void set(int input) throws RemoteException;

  public int get() throws RemoteException;
}
