package ObjectServiceImplementation;

import DataAccessObject.*;
import DtoObjects.*;
import Objects.*;

import java.util.List;

/**
 * Service dédié aux fonctionnalités accessibles aux enseignants.
 * Permet de consulter leurs cours, gérer les étudiants inscrits et ajouter des supports.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class EnseignantServiceImp {

    private final CoursOffertDao coursOffertDAO;
    private final MatiereDao matiereDAO;
    private final InscriptionDao inscriptionDAO;
    private final StudentDao etudiantDAO;
    private final SupportCoursDao supportCoursDAO;

    /**
     * Constructeur injectant les DAO nécessaires.
     */
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

    /**
     * Retourne la liste des cours enseignés par un enseignant.
     * Chaque cours est transformé en DTO contenant des informations utiles.
     *
     * @param idEnseignant identifiant de l'enseignant
     * @return liste des cours sous forme de DTO
     */
    public List<EnseignantCoursDto> consulterMesCours(int idEnseignant) {

        List<CoursOffert> cours = coursOffertDAO.getCoursByEnseignant(idEnseignant);

        return cours.stream().map(c -> {

            // Récupération des informations complémentaires
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

    /**
     * Retourne la liste des étudiants inscrits à un cours offert.
     *
     * @param idCoursOffert identifiant du cours offert
     * @return liste des étudiants sous forme de DTO
     */
    public List<StudentDansCoursDto> consulterEtudiantsDuCours(int idCoursOffert) {

        List<Inscription> inscriptions = inscriptionDAO.getInscriptionsByCours(idCoursOffert);

        return inscriptions.stream().map(inscription -> {

            Student student = etudiantDAO.find(inscription.getIdEtudiant());

            return new StudentDansCoursDto(
                    student.getId(),
                    student.getName(),
                    inscription.getStatut(),
                    inscription.getNote()
            );

        }).toList();
    }

    /**
     * Change le statut d'un étudiant dans un cours offert.
     *
     * @param idEtudiant identifiant de l'étudiant
     * @param idCoursOffert identifiant du cours offert
     * @param nouveauStatut nouveau statut à appliquer
     * @return true si la mise à jour a réussi
     */
    public boolean changerStatutEtudiant(int idEtudiant, int idCoursOffert, String nouveauStatut) {

        Inscription ins = inscriptionDAO.getInscription(idEtudiant, idCoursOffert);

        if (ins == null) {
            System.out.println("L'étudiant n'est pas inscrit à ce cours.");
            return false;
        }

        // Validation du statut
        if (!nouveauStatut.equals("passed") &&
                !nouveauStatut.equals("fail") &&
                !nouveauStatut.equals("on_going")) {

            System.out.println("Statut invalide.");
            return false;
        }

        return inscriptionDAO.updateStatut(idEtudiant, idCoursOffert, nouveauStatut);
    }

    /**
     * Ajoute un support de cours pour un cours offert.
     *
     * @param idCoursOffert identifiant du cours offert
     * @param chemin chemin du fichier support
     * @return true si l'ajout a réussi
     */
    public boolean ajouterSupportCours(int idCoursOffert, String chemin) {
        SupportCours support = new SupportCours(idCoursOffert, chemin);
        return supportCoursDAO.insertSupport(support);
    }

    /**
     * Attribue une note à un étudiant dans un cours offert.
     *
     * @param idEtudiant identifiant de l'étudiant
     * @param idCoursOffert identifiant du cours offert
     * @param note note à attribuer
     * @return true si la mise à jour a réussi
     */
    public boolean noterEtudiant(int idEtudiant, int idCoursOffert, double note) {

        Inscription ins = inscriptionDAO.getInscription(idEtudiant, idCoursOffert);

        if (ins == null) {
            System.out.println("L'étudiant n'est pas inscrit à ce cours.");
            return false;
        }

        return inscriptionDAO.updateNote(idEtudiant, idCoursOffert, note);
    }
}