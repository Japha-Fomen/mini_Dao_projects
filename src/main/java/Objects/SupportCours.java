package Objects;

/**
 * Représente un support de cours associé à un cours offert.
 * Contient un identifiant, un cours et un chemin de fichier.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class SupportCours {

    private int idSupport;
    private int idCoursOffert;
    private String chemin;

    /**
     * Constructeur complet incluant l'identifiant du support.
     */
    public SupportCours(int idSupport, int idCoursOffert, String chemin) {
        this.idSupport = idSupport;
        this.idCoursOffert = idCoursOffert;
        this.chemin = chemin;
    }

    /**
     * Constructeur utilisé lors de l'ajout d'un nouveau support.
     */
    public SupportCours(int idCoursOffert, String chemin) {
        this.idCoursOffert = idCoursOffert;
        this.chemin = chemin;
    }

    public int getIdSupport() { return idSupport; }
    public int getIdCoursOffert() { return idCoursOffert; }
    public String getChemin() { return chemin; }

    public void setChemin(String chemin) { this.chemin = chemin; }
}