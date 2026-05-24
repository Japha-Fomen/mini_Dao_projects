package Objects;

/**
 * Représente l'inscription d'un étudiant à un cours offert.
 * Contient le statut et la note éventuelle.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class Inscription {

    private int idEtudiant;
    private int idCoursOffert;
    private String statut;
    private Double note;

    /**
     * Constructeur complet d'une inscription.
     */
    public Inscription(int idEtudiant, int idCoursOffert, String statut, Double note) {
        this.idEtudiant = idEtudiant;
        this.idCoursOffert = idCoursOffert;
        this.statut = statut;
        this.note = note;
    }

    public int getIdEtudiant() { return idEtudiant; }
    public int getIdCoursOffert() { return idCoursOffert; }
    public String getStatut() { return statut; }
    public Double getNote() { return note; }

    public void setStatut(String statut) { this.statut = statut; }
    public void setNote(Double note) { this.note = note; }
}