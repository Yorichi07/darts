package com.darts.server.functions;

import java.util.HashMap;
import java.util.List;

import com.darts.server.model.Specialist;

public class HospitalRecords {

    public static HashMap<String,HashMap<Integer,List<Integer>>> doctors =  new HashMap<>();
    public static HashMap<Integer,Integer> patDocMap;

    private static void sortDocList(){

    }

    public static boolean insertDoc(Specialist doc){
        if(doctors.keySet().contains(doc.getSpeciality())){
            if(doctors.get(doc.getSpeciality()).keySet().contains(doc.getDocID())){
                System.out.println(doctors.toString());
                return false;
            }
            doctors.get(doc.getSpeciality()).put(doc.getDocID(), List.of());
            System.out.println(doctors.toString());
            return true;
        }
        doctors.put(doc.getSpeciality(),new HashMap<Integer,List<Integer>>());
        doctors.get(doc.getSpeciality()).put(doc.getDocID(), List.of());
        System.out.println(doctors.toString());
        return true;
    }

    public static boolean deleteDoc(Specialist doc){
        if(doctors.keySet().contains(doc.getSpeciality())){
            if(doctors.get(doc.getSpeciality()).keySet().contains(doc.getDocID())){
                doctors.get(doc.getSpeciality()).remove(doc.getDocID());
                return true;
            }
        }
        return false;
    }

    public static boolean insertPatDoc(Integer UID,Specialist doc){
        if(doctors.containsKey(doc.getSpeciality())){
            if(doctors.get(doc.getSpeciality()).containsKey(doc.getDocID())){
                doctors.get(doc.getSpeciality()).get(doc.getDocID()).add(UID);
                patDocMap.put(UID, doc.getDocID());
                return true;
            }
        }
        return false;
    }

    public static boolean insertPat(Integer UID, String speciality){
        if(doctors.containsKey(speciality)){
            if(!doctors.get(speciality).keySet().isEmpty()){
                Integer DID = doctors.get(speciality).keySet().iterator().next();
                doctors.get(speciality).get(DID).add(UID);
                patDocMap.put(UID, DID);
                return true;
            }
        }
        return false;
    }

    // Queue delete
    public static void deletePat(Integer UID){

    }

    public static Integer searchList(Integer UID){
        return 1;
    }

    
}
