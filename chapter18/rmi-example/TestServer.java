import java.rmi.Naming;

class TestServer {
  public static void main(String[] args) {
    try {
      TestRemoteImpl test = new TestRemoteImpl();
      System.out.println("Binding TestRemote");
      Naming.bind("TestRemote", test);
    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
