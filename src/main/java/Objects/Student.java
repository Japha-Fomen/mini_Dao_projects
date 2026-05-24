package Objects;

/**
 * Représente un étudiant du système.
 * Contient un identifiant, un nom et un âge.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class Student {

    private int id;
    private String name;
    private int age;

    /**
     * Constructeur complet d'un étudiant.
     */
    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", age=" + age + '}';
    }
}