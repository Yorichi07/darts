package com.darts;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //new patientDetailsRetrieval().getPatient(sc);
        // new Authentication().getHash(sc);


        hospitalretrieval.getHospital(sc);

        sc.close();
    }
}
