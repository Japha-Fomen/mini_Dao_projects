package Objects;

/**
 * Représente une matière enseignée dans le système.
 * Contient un code, un nom et une description.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class Matiere {

    private String idMatiere;
    private String nameMatiere;
    private String description;

    /**
     * Constructeur complet d'une matière.
     */
    public Matiere(String idMatiere, String nameMatiere, String description) {
        this.idMatiere = idMatiere;
        this.nameMatiere = nameMatiere;
        this.description = description;
    }

    public String getIdMatiere() { return idMatiere; }
    public void setIdMatiere(String idMatiere) { this.idMatiere = idMatiere; }

    public String getNameMatiere() { return nameMatiere; }
    public void setNameMatiere(String nameMatiere) { this.nameMatiere = nameMatiere; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Matiere{" + "idMatiere=" + idMatiere + ", name=" + nameMatiere + ", description=" + description + '}';
    }
}