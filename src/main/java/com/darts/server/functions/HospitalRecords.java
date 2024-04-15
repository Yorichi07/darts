package com.darts.server.functions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.darts.server.model.Specialist;
import com.darts.server.service.SpecialistService;

public class HospitalRecords {

    public static HashMap<String,HashMap<Integer,List<Integer>>> doctors =  new HashMap<>();
    public static HashMap<Integer,Integer> patDocMap;

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
                Iterator<Integer> docIterator = doctors.get(doc.getSpeciality()).get(doc.getDocID()).iterator();
                while(docIterator.hasNext()){
                    patDocMap.remove(docIterator.next());
                }
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
                Iterator<Integer> docIterator = doctors.get(speciality).keySet().iterator();
                while(docIterator.hasNext()){
                    int did = docIterator.next().intValue();
                    if(doctors.get(speciality).get(did).size() < doctors.get(speciality).get(DID).size()){
                        DID = did;
                    }
                }
                patDocMap.put(UID, DID);
                return true;
            }
        }
        return false;
    }

    public static Integer getPat(Integer DID){
        SpecialistService specialistService = new SpecialistService();
        Specialist doc = specialistService.getOneSpecialist(DID).get();
        if(doctors.get(doc.getSpeciality()).get(doc.getDocID()).isEmpty()){
           return -1; 
        }
        return doctors.get(doc.getSpeciality()).get(doc.getDocID()).get(0);
    }

    // Queue delete
    public static void deletePat(Integer UID){
        Integer DID = patDocMap.get(UID);
        SpecialistService specialistService = new SpecialistService();
        Specialist doc = specialistService.getOneSpecialist(DID).get();

        // Remove the first index
        doctors.get(doc.getSpeciality()).get(doc.getDocID()).remove(0);

        patDocMap.remove(UID);
    }

    public static Integer searchPatNum(Integer UID){
        if(patDocMap.keySet().contains(UID)){
        int DID = patDocMap.get(UID);
        SpecialistService specialistService = new SpecialistService();
        Specialist doc = specialistService.getOneSpecialist(DID).get();
        return doctors.get(doc.getSpeciality()).get(doc.getDocID()).indexOf(UID);
        }
        return -2;
    }

    
}
