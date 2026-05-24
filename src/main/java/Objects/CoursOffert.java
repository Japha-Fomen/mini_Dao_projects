package Objects;

/**
 * Représente un cours offert pour une session donnée.
 * Contient les informations sur la matière, l'enseignant, la session et l'année.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class CoursOffert {

    private int idCoursOffert;
    private String idMatiere;
    private int idEnseignant;
    private String session;
    private int annee;

    /**
     * Constructeur complet incluant l'identifiant du cours offert.
     */
    public CoursOffert(int idCoursOffert, String idMatiere, int idEnseignant, String session, int annee) {
        this.idCoursOffert = idCoursOffert;
        this.idMatiere = idMatiere;
        this.idEnseignant = idEnseignant;
        this.session = session;
        this.annee = annee;
    }

    /**
     * Constructeur utilisé lors de la création d'un nouveau cours offert.
     */
    public CoursOffert(String idMatiere, int idEnseignant, String session, int annee) {
        this.idMatiere = idMatiere;
        this.idEnseignant = idEnseignant;
        this.session = session;
        this.annee = annee;
    }

    public int getIdCoursOffert() { return idCoursOffert; }
    public String getIdMatiere() { return idMatiere; }
    public int getIdEnseignant() { return idEnseignant; }
    public String getSession() { return session; }
    public int getAnnee() { return annee; }

    public void setIdMatiere(String idMatiere) { this.idMatiere = idMatiere; }
    public void setIdEnseignant(int idEnseignant) { this.idEnseignant = idEnseignant; }
    public void setSession(String session) { this.session = session; }
    public void setAnnee(int annee) { this.annee = annee; }
}