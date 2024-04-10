package com.darts.server.functions;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import com.darts.server.model.Specialist;

public class DocAllocation {
    public Specialist allocateDoc(List<Specialist> specialists, int urgency){
        if(urgency==1){
            return allocateEmergencyDoctor(specialists);
        }
        else if(urgency==0){
            return allocateNonEmergencyDoctor(specialists);
        }
        else{
            throw new IllegalArgumentException("Invalid urgency value. Must be 0 or 1.");
        }
    }

    public Specialist allocateEmergencyDoctor(List<Specialist> specialists){
        List<Specialist> emerDoc=specialists.stream().filter(Specialist->"YES".equals(Specialist.getEmergency())).collect(Collectors.toList());

        if(emerDoc.isEmpty()){
            return null;
        }

        Random random = new Random();
        int index=random.nextInt(emerDoc.size());
        return emerDoc.get(index);
    }

    public Specialist allocateNonEmergencyDoctor(List<Specialist> specialists){
        List<Specialist> emerDoc=specialists.stream().filter(Specialist->"NO".equals(Specialist.getEmergency())).collect(Collectors.toList());

        if(emerDoc.isEmpty()){
            return null;
        }

        Random random = new Random();
        int index=random.nextInt(emerDoc.size());
        return emerDoc.get(index);
    }
}