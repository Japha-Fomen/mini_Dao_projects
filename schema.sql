-- ============================================================
--  Academic Management System — Database Schema
--  Author : Japha Fomen
-- ============================================================
--
--  RECOMMENDED : Create a dedicated database before running this script.
--
--    CREATE DATABASE academic_db
--      CHARACTER SET utf8mb4
--      COLLATE utf8mb4_unicode_ci;
--    USE academic_db;
--
--  Then update db.url in your db.properties accordingly :
--    db.url=jdbc:mysql://127.0.0.1:3306/academic_db
--
-- ============================================================


-- ── Students ─────────────────────────────────────────────────────────────────

CREATE TABLE Student (
    id   INT         NOT NULL,
    name VARCHAR(60) NOT NULL,
    age  TINYINT,
    CONSTRAINT pk_student PRIMARY KEY (id)
);


-- ── Subjects ─────────────────────────────────────────────────────────────────

CREATE TABLE Matiere (
    IdMatiere   VARCHAR(8)  NOT NULL,
    nom         VARCHAR(40),
    Description VARCHAR(80),
    CONSTRAINT pk_matiere PRIMARY KEY (IdMatiere)
);


-- ── Instructors ──────────────────────────────────────────────────────────────
--  Note : email column added to support instructor contact info.

CREATE TABLE Enseignant (
    id    INT          NOT NULL,
    nom   VARCHAR(50),
    email VARCHAR(100),
    CONSTRAINT pk_enseignant PRIMARY KEY (id)
);


-- ── Offered Courses ──────────────────────────────────────────────────────────
--  One row = one subject taught by one instructor in one session/year.
--  idCoursOffert is auto-incremented by the database.

CREATE TABLE CoursOffert (
    idCoursOffert INT         NOT NULL AUTO_INCREMENT,
    idMatiere     VARCHAR(8),
    idEnseignant  INT,
    session       VARCHAR(10),
    annee         INT         NOT NULL,
    CONSTRAINT pk_cours       PRIMARY KEY (idCoursOffert),
    FOREIGN KEY (idMatiere)   REFERENCES Matiere(IdMatiere) ON DELETE SET NULL,
    FOREIGN KEY (idEnseignant) REFERENCES Enseignant(id)
);


-- ── Enrollments ──────────────────────────────────────────────────────────────
--  Primary key is the pair (student, course) — a student can enrol once per course.
--  statut allowed values : 'on_going' | 'passed' | 'fail'

CREATE TABLE Inscription (
    idEtudiant    INT,
    idCoursOffert INT,
    statut        VARCHAR(9) NOT NULL DEFAULT 'on_going'
                  CHECK (statut IN ('on_going', 'passed', 'fail')),
    note          DOUBLE     DEFAULT NULL,
    CONSTRAINT pk_inscription PRIMARY KEY (idEtudiant, idCoursOffert),
    FOREIGN KEY (idEtudiant)    REFERENCES Student(id),
    FOREIGN KEY (idCoursOffert) REFERENCES CoursOffert(idCoursOffert)
);


-- ── Course Materials ─────────────────────────────────────────────────────────
--  ON DELETE CASCADE : if a course is deleted, all its materials are deleted too.

CREATE TABLE SupportCours (
    idSupport     INT          NOT NULL AUTO_INCREMENT,
    idCoursOffert INT,
    chemin        VARCHAR(255),
    CONSTRAINT pk_support PRIMARY KEY (idSupport),
    FOREIGN KEY (idCoursOffert) REFERENCES CoursOffert(idCoursOffert) ON DELETE CASCADE
);
