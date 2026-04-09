package Objects;

public class Inscription {
    private int idEtudiant;
    private int idCoursOffert;
    private String statut;
    private Double note;

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
