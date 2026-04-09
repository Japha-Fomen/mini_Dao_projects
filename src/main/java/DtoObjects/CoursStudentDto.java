package DtoObjects;

public class CoursStudentDto {
    private int idCoursOffert;

    // Informations sur la matière
    private String codeMatiere;
    private String nomMatiere;

    // Informations sur l'enseignant
    private String nomEnseignant;
    private String emailEnseignant;

    // Informations sur la session
    private String session;
    private int annee;

    // Informations sur l'inscription de l'étudiant
    private String statut;   // passed / fail / on_going
    private Double note;     // peut être null



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
