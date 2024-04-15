// package com.darts.server.functions;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.Random;
// import java.util.stream.Collectors;

// import com.darts.server.model.Specialist;

// public class DocAllocation {
//     public Specialist allocateDoc(List<Specialist> specialists, int urgency, String searchTerm){
//         if (urgency == 1) {
//             return allocateEmergencyDoctor(specialists);
//         } else if (urgency == 0) {
//             return allocateNonEmergencyDoctor(specialists, searchTerm);
//         } else {
//             throw new IllegalArgumentException("Invalid urgency value. Must be 0 or 1.");
//         }
//     }
    

//     public Specialist allocateEmergencyDoctor(List<Specialist> specialists){
//         List<Specialist> emerDoc=specialists.stream().filter(Specialist->"YES".equals(Specialist.getEmergency())).collect(Collectors.toList());

//         if(emerDoc.isEmpty()){
//             return null;
//         }

//         Random random = new Random();
//         int index=random.nextInt(emerDoc.size());
//         return emerDoc.get(index);
//     }

//     public Specialist allocateNonEmergencyDoctor(List<Specialist> specialists, String searchTerm) {
//         List<Specialist> filteredList = new ArrayList<>();

//         for (Specialist specialist : specialists) {
//             if ("NO".equalsIgnoreCase(specialist.getEmergency()) && 
//                 specialist.getDocName().equalsIgnoreCase(searchTerm) ||
//                 specialist.getSpeciality().equalsIgnoreCase(searchTerm)) {
//                 filteredList.add(specialist);
//             }
//         }
    
//         if (filteredList.isEmpty()) {
//             return null;
//         }

//         Random random = new Random();
//         int index = random.nextInt(filteredList.size());
//         return filteredList.get(index);
//     }
// }