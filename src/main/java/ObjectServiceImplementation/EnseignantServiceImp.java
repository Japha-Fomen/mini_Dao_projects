package ObjectServiceImplementation;
import DataAccessObject.*;
import DtoObjects.*;
import Objects.*;

import java.util.List;

public class EnseignantServiceImp {
    private final CoursOffertDao coursOffertDAO;
    private final MatiereDao matiereDAO;
    private final InscriptionDao inscriptionDAO;
    private final StudentDao etudiantDAO;
    private final SupportCoursDao supportCoursDAO;

    public EnseignantServiceImp(CoursOffertDao coursOffertDAO,
                                 MatiereDao matiereDAO,
                                 InscriptionDao inscriptionDAO,
                                 StudentDao etudiantDAO,
                                 SupportCoursDao supportCoursDAO) {

        this.coursOffertDAO = coursOffertDAO;
        this.matiereDAO = matiereDAO;
        this.inscriptionDAO = inscriptionDAO;
        this.etudiantDAO = etudiantDAO;
        this.supportCoursDAO = supportCoursDAO;
    }
    public List<EnseignantCoursDto> consulterMesCours(int idEnseignant) {

        List<CoursOffert> cours = coursOffertDAO.getCoursByEnseignant(idEnseignant);

        return cours.stream().map(c -> {

            Matiere mat = matiereDAO.findMatiere(c.getIdMatiere());
            int nbEtudiants = inscriptionDAO.getInscriptionsByCours(c.getIdCoursOffert()).size();
            int nbSupports = supportCoursDAO.getSupportsByCours(c.getIdCoursOffert()).size();

            return new EnseignantCoursDto(
                    c.getIdCoursOffert(),
                    mat.getIdMatiere(),
                    mat.getNameMatiere(),
                    c.getSession(),
                    c.getAnnee(),
                    nbEtudiants,
                    nbSupports
            );

        }).toList();
    }
    public List<StudentDansCoursDto> consulterEtudiantsDuCours(int idCoursOffert) {
        List<Inscription> inscriptions=inscriptionDAO.getInscriptionsByCours(idCoursOffert);
        return inscriptions.stream().map(inscription -> {
            Student students=etudiantDAO.find(inscription.getIdEtudiant());
            return new StudentDansCoursDto(students.getId(), students.getName(), inscription.getStatut(), inscription.getNote()
            );
        }).toList();
    }
    public boolean changerStatutEtudiant(int idEtudiant, int idCoursOffert, String nouveauStatut) {

        // Vérifier que l'étudiant est inscrit
        Inscription ins = inscriptionDAO.getInscription(idEtudiant, idCoursOffert);
        if (ins == null) {
            System.out.println("L'étudiant n'est pas inscrit à ce cours.");
            return false;
        }

        // Vérifier que le statut est valide
        if (!nouveauStatut.equals("passed") &&
                !nouveauStatut.equals("fail") &&
                !nouveauStatut.equals("on_going")) {

            System.out.println("Statut invalide.");
            return false;
        }

        return inscriptionDAO.updateStatut(idEtudiant, idCoursOffert, nouveauStatut);
    }

    public boolean ajouterSupportCours(int idCoursOffert, String chemin) {
        SupportCours support = new SupportCours(idCoursOffert, chemin);
        return supportCoursDAO.insertSupport(support);
    }

    public boolean noterEtudiant(int idEtudiant, int idCoursOffert, double note) {

        // Vérifier que l'étudiant est inscrit
        Inscription ins = inscriptionDAO.getInscription(idEtudiant, idCoursOffert);
        if (ins == null) {
            System.out.println("L'étudiant n'est pas inscrit à ce cours.");
            return false;
        }

        return inscriptionDAO.updateNote(idEtudiant, idCoursOffert, note);
    }

}
