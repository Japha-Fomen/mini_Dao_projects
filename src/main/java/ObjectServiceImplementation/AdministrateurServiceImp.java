package ObjectServiceImplementation;

import DataAccessObject.*;
import Objects.*;
import java.util.List;

/**
 * Service permettant à l'administrateur de gérer l'ensemble des entités du système.
 * L'administrateur possède tous les droits sauf la gestion directe des supports de cours.
 *
 * @author Japha Fomen
 * @version 1.0
 */
public class AdministrateurServiceImp {

    private final MatiereDao matiereDao;
    private final CoursOffertDao coursOffertDao;
    private final InscriptionDao inscriptionDao;
    private final SupportCoursDao supportCoursDao;
    private final EnseignantDao enseignantDao;
    private final StudentDao etudiantDAO;

    /**
     * Constructeur du service administrateur.
     *
     * @param matiereDao DAO pour la gestion des matières
     * @param coursOffertDao DAO pour la gestion des cours offerts
     * @param inscriptionDao DAO pour la gestion des inscriptions
     * @param supportCoursDao DAO pour la gestion des supports de cours
     * @param enseignantDao DAO pour la gestion des enseignants
     * @param etudiantDAO DAO pour la gestion des étudiants
     */
    public AdministrateurServiceImp(MatiereDao matiereDao,
                                    CoursOffertDao coursOffertDao,
                                    InscriptionDao inscriptionDao,
                                    SupportCoursDao supportCoursDao,
                                    EnseignantDao enseignantDao,
                                    StudentDao etudiantDAO) {

        this.matiereDao = matiereDao;
        this.coursOffertDao = coursOffertDao;
        this.inscriptionDao = inscriptionDao;
        this.supportCoursDao = supportCoursDao;
        this.enseignantDao = enseignantDao;
        this.etudiantDAO = etudiantDAO;
    }

    // -------------------------------------------------------------------------
    // ÉTUDIANTS
    // -------------------------------------------------------------------------

    /**
     * Enregistre un nouvel étudiant dans le système.
     *
     * @param student étudiant à enregistrer
     * @return true si l'opération réussit, false sinon
     */
    public boolean enregistrerEtudiant(Student student) {
        try {
            etudiantDAO.insert(student);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Met à jour les informations d'un étudiant.
     *
     * @param student étudiant à mettre à jour
     * @return true si la mise à jour réussit
     */
    public boolean updateStudent(Student student) {
        return etudiantDAO.update(student);
    }

    /**
     * Supprime un étudiant du système.
     *
     * @param student étudiant à supprimer
     * @return true si la suppression réussit
     */
    public boolean deleteStudent(Student student) {
        try {
            etudiantDAO.delete(student.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retourne la liste complète des étudiants.
     *
     * @return liste des étudiants
     */
    public List<Student> listEtudiants() {
        return etudiantDAO.findAll();
    }

    /**
     * Retourne la liste des étudiants inscrits à un cours offert.
     *
     * @param idCoursOffert identifiant du cours offert
     * @return liste des étudiants inscrits
     */
    public List<Student> listEtudiantpourCoursOffert(int idCoursOffert) {
        return inscriptionDao.getInscriptionsByCours(idCoursOffert)
                .stream()
                .map(ins -> etudiantDAO.find(ins.getIdEtudiant()))
                .toList();
    }

    // -------------------------------------------------------------------------
    // ENSEIGNANTS
    // -------------------------------------------------------------------------

    /**
     * Enregistre un nouvel enseignant.
     *
     * @param enseignant enseignant à enregistrer
     * @return true si l'opération réussit
     */
    public boolean enregistrerEnseignant(Enseignant enseignant) {
        try {
            enseignantDao.insert(enseignant);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Met à jour les informations d'un enseignant.
     *
     * @param enseignant enseignant à mettre à jour
     * @return true si la mise à jour réussit
     */
    public boolean updateEnseignant(Enseignant enseignant) {
        return enseignantDao.updateEnseignant(enseignant);
    }

    /**
     * Supprime un enseignant du système.
     *
     * @param enseignant enseignant à supprimer
     * @return true si la suppression réussit
     */
    public boolean deleteEnseignant(Enseignant enseignant) {
        return enseignantDao.deleteEnseignant(enseignant.getId());
    }

    /**
     * Retourne la liste complète des enseignants.
     *
     * @return liste des enseignants
     */
    public List<Enseignant> enseignantList() {
        return enseignantDao.getAllEnseignants();
    }

    // -------------------------------------------------------------------------
    // MATIÈRES
    // -------------------------------------------------------------------------

    /**
     * Enregistre une nouvelle matière.
     *
     * @param matiere matière à enregistrer
     * @return true si l'opération réussit
     */
    public boolean enregistrerMatiere(Matiere matiere) {
        try {
            matiereDao.insert(matiere);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Supprime une matière.
     *
     * @param matiere matière à supprimer
     * @return true si la suppression réussit
     */
    public boolean deleteMatiere(Matiere matiere) {
        try {
            matiereDao.delete(matiere.getIdMatiere());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retourne la liste des matières.
     *
     * @return liste des matières
     */
    public List<Matiere> listMatieres() {
        return matiereDao.findAll();
    }

    // -------------------------------------------------------------------------
    // COURS OFFERTS
    // -------------------------------------------------------------------------

    /**
     * Enregistre un nouveau cours offert.
     *
     * @param coursOffert cours offert à enregistrer
     * @return true si l'opération réussit
     */
    public boolean enregistrerCoursOffert(CoursOffert coursOffert) {
        return coursOffertDao.insert(coursOffert);
    }

    /**
     * Met à jour un cours offert.
     *
     * @param coursOffert cours offert à mettre à jour
     * @return true si la mise à jour réussit
     */
    public boolean updateCoursOffert(CoursOffert coursOffert) {
        return coursOffertDao.updateCours(coursOffert);
    }

    /**
     * Supprime un cours offert ainsi que ses inscriptions et supports associés.
     *
     * @param coursOffert cours offert à supprimer
     * @return true si la suppression réussit
     */
    public boolean deleteCoursOffert(CoursOffert coursOffert) {

        // Supprimer les supports associés
        supportCoursDao.supprimerSupportsDuCours(coursOffert.getIdCoursOffert());

        // Supprimer les inscriptions associées
        inscriptionDao.getInscriptionsByCours(coursOffert.getIdCoursOffert())
                .forEach(ins -> inscriptionDao.desinscrire(ins.getIdEtudiant(), coursOffert.getIdCoursOffert()));

        return coursOffertDao.supprimerCours(coursOffert.getIdCoursOffert());
    }

    /**
     * Retourne la liste de tous les cours offerts.
     *
     * @return liste des cours offerts
     */
    public List<CoursOffert> listCoursOfferts() {
        return coursOffertDao.getAllCours();
    }

    // -------------------------------------------------------------------------
    // INSCRIPTIONS
    // -------------------------------------------------------------------------

    /**
     * Enregistre une nouvelle inscription.
     *
     * @param inscription inscription à enregistrer
     * @return true si l'opération réussit
     */
    public boolean enregistrerInscription(Inscription inscription) {
        return inscriptionDao.inscrire(inscription);
    }

    /**
     * Supprime une inscription.
     *
     * @param inscription inscription à supprimer
     * @return true si la suppression réussit
     */
    public boolean deleteInscription(Inscription inscription) {
        return inscriptionDao.desinscrire(inscription.getIdEtudiant(), inscription.getIdCoursOffert());
    }

    /**
     * Retourne toutes les inscriptions du système (tous cours confondus).
     *
     * @return liste de toutes les inscriptions
     */
    public List<Inscription> listAllInscriptions() {
        return listCoursOfferts().stream()
                .flatMap(c -> inscriptionDao.getInscriptionsByCours(c.getIdCoursOffert()).stream())
                .toList();
    }

    // -------------------------------------------------------------------------
    // AUTHENTIFICATION
    // -------------------------------------------------------------------------

    /**
     * Recherche un étudiant par son identifiant.
     * Retourne null si aucun étudiant ne correspond.
     *
     * @param id identifiant de l'étudiant
     * @return l'étudiant correspondant, ou null s'il n'existe pas
     */
    public Student findStudentById(int id) {
        return etudiantDAO.find(id);
    }

    /**
     * Recherche un enseignant par son identifiant.
     * Retourne null si aucun enseignant ne correspond.
     *
     * @param id identifiant de l'enseignant
     * @return l'enseignant correspondant, ou null s'il n'existe pas
     */
    public Enseignant findEnseignantById(int id) {
        return enseignantDao.getEnseignantById(id);
    }
}