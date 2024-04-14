package com.darts.server.functions;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.darts.server.model.Hospital;
import com.darts.server.service.HospitalService;;

@Component
public class NearestHospital {
    private final HospitalService hosSer;

    @Autowired
    public NearestHospital(HospitalService hosSer){
        this.hosSer=hosSer;
    };
    public List<Hospital> getNearestHospital(double curLat, double curLong){
        List<Hospital> hos = hosSer.getAllHospitals();

        List<Hospital> nearestHos = hos.stream().filter(h->h.getBEDS()>0).sorted(Comparator.comparingDouble(h->calDis(curLat,curLong,h.getLATITUDE(),h.getLONGITUDE()))).limit(5).collect(Collectors.toList());
        
        return nearestHos;
    }

    //using haversine formula to cal dis btw two points
    private double calDis(double curLat, double curLong, double lat1, double long1){
        final int R = 6371; //radius of earth

        double latDis=Math.toRadians(lat1-curLat);
        double lonDis=Math.toRadians(long1-curLong);

        double a = Math.sin(latDis / 2) * Math.sin(latDis / 2)
        + Math.cos(Math.toRadians(curLat)) * Math.cos(Math.toRadians(lat1))
        * Math.sin(lonDis / 2) * Math.sin(lonDis / 2);

        double c=2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
        double distance=R*c;    //convert to meters

        return distance;
    }
}
