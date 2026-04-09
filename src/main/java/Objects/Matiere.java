package Objects;

public class Matiere {
    private String idMatiere;
    private String nameMatiere;
    private String Description;
   public Matiere(String idMatiere, String nameMatiere, String Description) {
        this.idMatiere = idMatiere;
        this.nameMatiere = nameMatiere;
        this.Description = Description;
    }
    public String getIdMatiere() {
        return idMatiere;
    }
    public void setIdMatiere(String idMatiere) {
        this.idMatiere = idMatiere;
    }
    public String getNameMatiere() {
        return nameMatiere;
    }
    public void setNameMatiere(String nameMatiere) {
        this.nameMatiere = nameMatiere;
    }
    public String getDescription() {
        return Description;
    }
    public void setDescription(String Description) {
        this.Description = Description;
    }
    @Override
    public String toString() {
       return "Matiere{" + "idMatiere=" + idMatiere + ", name=" + nameMatiere + ", description=" + Description + '}';
    }
    
}
