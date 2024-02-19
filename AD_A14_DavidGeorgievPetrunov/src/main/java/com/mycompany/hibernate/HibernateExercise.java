package com.mycompany.hibernate;

import hibernate.Student;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Query;

public class HibernateExercise {

    static SessionFactory factory;

    // Main
    public static void main(String[] args) {

        Configuration cfg = new Configuration();
        cfg.configure("/com/mycompany/config/hibernate.cfg.xml");
        factory = cfg.buildSessionFactory();

        // Add
        insertStudent("Antonio", "Martinez",16, "antonio@email.com", "971112312");
        insertStudent("Diego", "Martinez",17, "diego@email.com", "971888828");
        insertStudent("Lucas", "Rodriguez",18, "lucas@email.com", "971822827");
        insertStudent("Manolo", "Iglesias",19, "manolo@email.com", "971888821");

        // Update
        updateStudent(1, "Change name", "Martinez",16, "antonio@email.com", "971112312");

        // Delete
        deleteStudent(2);

        // Get one item
        System.out.println("Get name of student 1: " + getStudent(1).getName());

        // Print all students
        System.out.println("Students..............: ");
        for (Student student : listStudents()) {
            System.out.println(student.getId() + " : " + student.getName());
        }

        // Close and finish
        factory.close();
    }

    // Insert a student
    private static void insertStudent(String nombre, String lastname, int age, String email, String phone) {
        Session session = null;
        try {
            session = factory.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                Student newStudent = new Student();
                newStudent.setName(nombre);
                newStudent.setLastname(lastname);
                newStudent.setAge(age);
                newStudent.setEmail(email);
                newStudent.setPhone(phone);

                session.save(newStudent);

                transaction.commit();

                System.out.println("Student inserted successfully!");
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void updateStudent(int id, String nombre, String lastname, int age, String email, String phone) {
        Session session = null;
        try {
            session = factory.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                Student existingStudent = (Student) session.get(Student.class, id);

                if (existingStudent != null) {

                    existingStudent.setName(nombre);
                    existingStudent.setLastname(lastname);
                    existingStudent.setAge(age);
                    existingStudent.setEmail(email);
                    existingStudent.setPhone(phone);

                    session.update(existingStudent);

                    transaction.commit();

                    System.out.println("Student updated successfully!");
                } else {
                    System.out.println("Student with ID " + id + " not found.");
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void deleteStudent(int id) {
        Session session = null;
        try {
            session = factory.openSession();
            Transaction transaction = null;

            try {
                transaction = session.beginTransaction();

                Student existingStudent = (Student) session.get(Student.class, id);

                if (existingStudent != null) {
                    session.delete(existingStudent);

                    transaction.commit();

                    System.out.println("Student deleted successfully!");
                } else {
                    System.out.println("Student with ID " + id + " not found.");
                }
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
                e.printStackTrace();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static Student getStudent(int id) {
        Session session = null;
        try {
            session = factory.openSession();

            Student student = (Student) session.get(Student.class, id);

            if (student != null) {
                return student;
            } else {
                System.out.println("Student with ID " + id + " not found.");
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return null;
    }

    private static List<Student> listStudents() {
        List<Student> students = new ArrayList<>();
        Session session = null;

        try {
            session = factory.openSession();

            String hql = "FROM Student";
            Query query = session.createQuery(hql);
            students = query.list();

        } finally {
            if (session != null) {
                session.close();
            }
        }

        return students;
    }
}
