package DtoObjects;

/**
 * Représente les informations d'un cours suivi par un étudiant.
 * Combine les données du cours, de la matière, de l'enseignant et de l'inscription.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class CoursStudentDto {

    private int idCoursOffert;
    private String codeMatiere;
    private String nomMatiere;
    private String nomEnseignant;
    private String emailEnseignant;
    private String session;
    private int annee;
    private String statut;
    private Double note;

    /**
     * Constructeur complet du DTO étudiant-cours.
     */
    public CoursStudentDto(int idCoursOffert,
                           String codeMatiere,
                           String nomMatiere,
                           String nomEnseignant,
                           String emailEnseignant,
                           String session,
                           int annee,
                           String statut,
                           Double note) {

        this.idCoursOffert = idCoursOffert;
        this.codeMatiere = codeMatiere;
        this.nomMatiere = nomMatiere;
        this.nomEnseignant = nomEnseignant;
        this.emailEnseignant = emailEnseignant;
        this.session = session;
        this.annee = annee;
        this.statut = statut;
        this.note = note;
    }

    public int getIdCoursOffert() { return idCoursOffert; }
    public String getCodeMatiere() { return codeMatiere; }
    public String getNomMatiere() { return nomMatiere; }
    public String getNomEnseignant() { return nomEnseignant; }
    public String getEmailEnseignant() { return emailEnseignant; }
    public String getSession() { return session; }
    public int getAnnee() { return annee; }
    public String getStatut() { return statut; }
    public Double getNote() { return note; }
}