package org.example;

import DataAccessObject.DataBaseConnection;
import DataAccessObject.MatiereDao;
import DataAccessObject.StudentDao;
import Objects.Matiere;
import Objects.Student;

import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        StudentDao dao=new StudentDao();
        MatiereDao dao2=new MatiereDao();
        List<Matiere> matiereList=dao2.findAll();
       List<Student> StudentList=dao.findAll();
       for(Student s:StudentList){
           System.out.println(s.toString());
       }
       for(Matiere m:matiereList){
           System.out.println(m.toString());
       }
        //creation de 5 eleves

//        Student st1=new Student(8,"baki",18);
//        Student st2=new Student(9,"bold",19);
//        Student st3=new Student(10,"bill",20);
//        Student st4=new Student(11,"blaki",17);
//        Student st0=new Student(12,"laki",18);
//        try {
//            dao.insert(st0);
//            dao.insert(st1);
//            dao.insert(st2);
//            dao.insert(st3);
//            dao.insert(st4);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }

        System.out.println("test1");
    }
}