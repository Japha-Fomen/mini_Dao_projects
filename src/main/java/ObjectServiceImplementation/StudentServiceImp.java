package ObjectServiceImplementation;

import DataAccessObject.*;
import DtoObjects.CoursStudentDto;
import Objects.*;

import java.util.List;

public class StudentServiceImp {
    private MatiereDao matiereDao;
    private CoursOffertDao coursOffertDao;
    private InscriptionDao inscriptionDao;
    private SupportCoursDao supportCoursDao;
    private EnseignantDao enseignantDao;
    public StudentServiceImp(InscriptionDao inscriptionDao, SupportCoursDao supportCoursDao, MatiereDao matiereDao, CoursOffertDao coursOffertDao, EnseignantDao enseignantDao) {
        this.inscriptionDao = inscriptionDao;
        this.supportCoursDao = supportCoursDao;
        this.matiereDao = matiereDao;
        this.coursOffertDao = coursOffertDao;
        this.enseignantDao = enseignantDao;
    }

    public List<CoursStudentDto>ConsulterMesCours(int idEtudiant) {
        //1 RECUPERER LA LISTE DES INSCRIPTIONS
        List<Inscription> inscriptions=inscriptionDao.getInscriptionsByEtudiant(idEtudiant);
        //2 transformer chaque inscription en cours etudiant Dto
        return inscriptions.stream().map(inscription -> {
            //recuperer le cours offert
            CoursOffert cours=coursOffertDao.getCoursById(inscription.getIdCoursOffert());
            //recuperer la matiere associee
            Matiere matiere=matiereDao.findMatiere(cours.getIdMatiere());
            //recuperer enseignant
            Enseignant enseignant=enseignantDao.getEnseignantById(cours.getIdEnseignant());
            //construction du dto
            return new CoursStudentDto(cours.getIdCoursOffert(), matiere.getIdMatiere(),
                    matiere.getNameMatiere(), enseignant.getNom(), enseignant.getEmail(),
                    cours.getSession(), cours.getAnnee(), inscription.getStatut(), inscription.getNote());
        }).toList();
    }

    public boolean abandonnerCours(int idEtudiant, int idCoursOffert) {

        // Vérifier que l'étudiant est inscrit
        Inscription inscription = inscriptionDao.getInscription(idEtudiant, idCoursOffert);
        if (inscription == null) {
            System.out.println("L'étudiant n'est pas inscrit à ce cours.");
            return false;
        }

        // Vérifier que le cours n'est pas déjà terminé
        if (!inscription.getStatut().equals("on_going")) {
            System.out.println("Impossible d'abandonner un cours déjà terminé.");
            return false;
        }

        // Désinscription
        return inscriptionDao.desinscrire(idEtudiant, idCoursOffert);
    }
    public List<String> consulterSupportsCours(int idCoursOffert) {

        // Vérifier que le cours existe
        CoursOffert cours = coursOffertDao.getCoursById(idCoursOffert);
        if (cours == null) {
            System.out.println("Cours inexistant.");
            return List.of();
        }

        // Retourner les supports
        List<SupportCours> supportCours =supportCoursDao.getSupportsByCours(idCoursOffert);
        return supportCours.stream().map(SupportCours::getChemin).toList();
    }

}
