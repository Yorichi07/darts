package app.src.main.java;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        new patientDetailsRetrieval().getPatient(sc);
        
        new App();
        sc.close();
    }
}