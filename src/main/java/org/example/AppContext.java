package org.example;

import DataAccessObject.*;
import ObjectServiceImplementation.*;

/**
 * Point d'accès unique à tous les services de l'application.
 *
 * LEÇON — Pourquoi ce pattern ?
 * Les services sont instanciés une seule fois (singletons statiques).
 * Les controllers ne créent jamais de DAO ou de service eux-mêmes :
 * ils les récupèrent via AppContext. C'est un "Service Locator" simplifié.
 *
 * Alternative plus avancée : injection de dépendances (Spring, Guice),
 * mais pour une app JavaFX autonome, ce pattern est parfaitement suffisant.
 */
public class AppContext {

    // ── DAOs (sans état, réutilisables) ─────────────────────────────────────
    private static final StudentDao      studentDao      = new StudentDao();
    private static final EnseignantDao   enseignantDao   = new EnseignantDao();
    private static final MatiereDao      matiereDao      = new MatiereDao();
    private static final CoursOffertDao  coursOffertDao  = new CoursOffertDao();
    private static final InscriptionDao  inscriptionDao  = new InscriptionDao();
    private static final SupportCoursDao supportCoursDao = new SupportCoursDao();

    // ── Services (consomment les DAOs) ───────────────────────────────────────
    public static final StudentServiceImp studentService = new StudentServiceImp(
            inscriptionDao, supportCoursDao, matiereDao, coursOffertDao, enseignantDao
    );

    public static final EnseignantServiceImp enseignantService = new EnseignantServiceImp(
            coursOffertDao, matiereDao, inscriptionDao, studentDao, supportCoursDao
    );

    public static final AdministrateurServiceImp adminService = new AdministrateurServiceImp(
            matiereDao, coursOffertDao, inscriptionDao, supportCoursDao, enseignantDao, studentDao
    );

    private AppContext() {}
}
