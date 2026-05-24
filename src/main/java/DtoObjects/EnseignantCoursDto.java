package DtoObjects;

/**
 * Représente un cours offert du point de vue d'un enseignant.
 * Contient les informations sur la matière, la session et les statistiques du cours.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class EnseignantCoursDto {

    private int idCoursOffert;
    private String codeMatiere;
    private String nomMatiere;
    private String session;
    private int annee;
    private int nbEtudiants;
    private int nbSupports;

    /**
     * Constructeur complet du DTO enseignant-cours.
     */
    public EnseignantCoursDto(int idCoursOffert,
                              String codeMatiere,
                              String nomMatiere,
                              String session,
                              int annee,
                              int nbEtudiants,
                              int nbSupports) {

        this.idCoursOffert = idCoursOffert;
        this.codeMatiere = codeMatiere;
        this.nomMatiere = nomMatiere;
        this.session = session;
        this.annee = annee;
        this.nbEtudiants = nbEtudiants;
        this.nbSupports = nbSupports;
    }

    public int getIdCoursOffert() { return idCoursOffert; }
    public String getCodeMatiere() { return codeMatiere; }
    public String getNomMatiere() { return nomMatiere; }
    public String getSession() { return session; }
    public int getAnnee() { return annee; }
    public int getNbEtudiants() { return nbEtudiants; }
    public int getNbSupports() { return nbSupports; }
}