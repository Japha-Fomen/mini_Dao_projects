package DtoObjects;

/**
 * Représente un étudiant inscrit dans un cours offert.
 * Contient les informations d'identité, de statut et de note.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class StudentDansCoursDto {

    private int idEtudiant;
    private String nomComplet;
    private String statut;
    private Double note;

    /**
     * Constructeur complet du DTO étudiant-dans-cours.
     */
    public StudentDansCoursDto(int idEtudiant, String nomComplet, String statut, Double note) {
        this.idEtudiant = idEtudiant;
        this.nomComplet = nomComplet;
        this.statut = statut;
        this.note = note;
    }

    public int getIdEtudiant() { return idEtudiant; }
    public String getNomComplet() { return nomComplet; }
    public String getStatut() { return statut; }
    public Double getNote() { return note; }
}