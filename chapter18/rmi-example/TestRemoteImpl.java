import java.rmi.RemoteException;
import java.rmi.server.*;

class TestRemoteImpl extends UnicastRemoteObject implements TestRemote {

  private int testInstanceVariable;

  public TestRemoteImpl() throws RemoteException {};

  public String test(String input) {
    return "Hello, " + input + ", from RMI Server!";
  }

  public void set(int input) {
    testInstanceVariable = input;
  }

  public int get() {
    return testInstanceVariable;
  }
}
