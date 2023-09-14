import java.util.ArrayList;
import java.util.List;

class BeTheCompiler {
  public static void main(String[] args) {
    // ArrayList<Dog> dogs1 = new ArrayList<Animal>();
    // ArrayList<Animal> animals1 = new ArrayList<Dog>();
    List<Animal> list = new ArrayList<Animal>();
    ArrayList<Dog> dogs = new ArrayList<Dog>();
    // ArrayList<Animal> animals = dogs;
    List<Dog> dogList = dogs;
    ArrayList<Object> objects = new ArrayList<Object>();
    List<Object> objList = objects;
    // ArrayList<Object> objs = new ArrayList<Dog>();
  }
}
