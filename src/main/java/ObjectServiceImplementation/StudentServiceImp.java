package ObjectServiceImplementation;

import DataAccessObject.*;
import DtoObjects.CoursStudentDto;
import Objects.*;

import java.util.List;

/**
 * Service dédié aux fonctionnalités accessibles aux étudiants.
 * Permet de consulter leurs cours, abandonner un cours et accéder aux supports.
 *
 * Auteur : Japha Fomen
 * Version : 1.0
 */
public class StudentServiceImp {

    private MatiereDao matiereDao;
    private CoursOffertDao coursOffertDao;
    private InscriptionDao inscriptionDao;
    private SupportCoursDao supportCoursDao;
    private EnseignantDao enseignantDao;

    /**
     * Constructeur injectant les DAO nécessaires.
     */
    public StudentServiceImp(InscriptionDao inscriptionDao,
                             SupportCoursDao supportCoursDao,
                             MatiereDao matiereDao,
                             CoursOffertDao coursOffertDao,
                             EnseignantDao enseignantDao) {

        this.inscriptionDao = inscriptionDao;
        this.supportCoursDao = supportCoursDao;
        this.matiereDao = matiereDao;
        this.coursOffertDao = coursOffertDao;
        this.enseignantDao = enseignantDao;
    }

    /**
     * Retourne la liste des cours auxquels un étudiant est inscrit.
     * Chaque inscription est transformée en DTO complet.
     *
     * @param idEtudiant identifiant de l'étudiant
     * @return liste des cours sous forme de DTO
     */
    public List<CoursStudentDto> ConsulterMesCours(int idEtudiant) {

        List<Inscription> inscriptions = inscriptionDao.getInscriptionsByEtudiant(idEtudiant);

        return inscriptions.stream().map(inscription -> {

            CoursOffert cours = coursOffertDao.getCoursById(inscription.getIdCoursOffert());
            Matiere matiere = matiereDao.findMatiere(cours.getIdMatiere());
            Enseignant enseignant = enseignantDao.getEnseignantById(cours.getIdEnseignant());

            return new CoursStudentDto(
                    cours.getIdCoursOffert(),
                    matiere.getIdMatiere(),
                    matiere.getNameMatiere(),
                    enseignant.getNom(),
                    enseignant.getEmail(),
                    cours.getSession(),
                    cours.getAnnee(),
                    inscription.getStatut(),
                    inscription.getNote()
            );

        }).toList();
    }

    /**
     * Permet à un étudiant d'abandonner un cours auquel il est inscrit.
     * L'abandon n'est possible que si le cours est en cours.
     *
     * @param idEtudiant identifiant de l'étudiant
     * @param idCoursOffert identifiant du cours offert
     * @return true si l'abandon a réussi
     */
    public boolean abandonnerCours(int idEtudiant, int idCoursOffert) {

        Inscription inscription = inscriptionDao.getInscription(idEtudiant, idCoursOffert);

        if (inscription == null) {
            System.out.println("L'étudiant n'est pas inscrit à ce cours.");
            return false;
        }

        if (!"on_going".equals(inscription.getStatut())) {
            System.out.println("Impossible d'abandonner un cours déjà terminé.");
            return false;
        }

        return inscriptionDao.desinscrire(idEtudiant, idCoursOffert);
    }

    /**
     * Retourne la liste des supports associés à un cours offert.
     *
     * @param idCoursOffert identifiant du cours offert
     * @return liste des chemins des supports
     */
    public List<String> consulterSupportsCours(int idCoursOffert) {

        CoursOffert cours = coursOffertDao.getCoursById(idCoursOffert);

        if (cours == null) {
            System.out.println("Cours inexistant.");
            return List.of();
        }

        List<SupportCours> supports = supportCoursDao.getSupportsByCours(idCoursOffert);

        return supports.stream().map(SupportCours::getChemin).toList();
    }
}