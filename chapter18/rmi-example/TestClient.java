import java.rmi.*;

class TestClient {
  public static void main(String[] args) {
    try {
      TestRemote test = (TestRemote) Naming.lookup("rmi://localhost/TestRemote");
      System.out.println(test.test("TestClient"));

      String fmt = "TestRemote instance variable: %d";

      int val = test.get();
      System.out.println(String.format(fmt, val));

      test.set(val + 1);

      val = test.get();
      System.out.println(String.format(fmt, val));

    } catch(Exception ex) {
      ex.printStackTrace();
    }
  }
}
