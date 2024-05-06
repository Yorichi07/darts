package com.darts.server.functions;

import org.jpmml.evaluator.*;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import org.json.JSONArray;

import com.opencsv.CSVReader;

public class DecisionTreeSymp {

    public static List<Object> jsonArray;
    private static Evaluator evaluator;
    private static ArrayList<String[]> disease = new ArrayList<String[]>();

    public static void initObjs(){

        try {
            @SuppressWarnings("resource")
            String jsonString = new Scanner(new File("./ML/Test_Col.json")).useDelimiter("\\Z").next();
            jsonArray = new JSONArray(jsonString).toList();

            evaluator = new LoadingModelEvaluatorBuilder().setLocatable(false).load(new File("./ML/model.pmml")).build();
            
            FileReader fileReader = new FileReader("./ML/Doctor_versus_Disease.csv");
            CSVReader reader = new CSVReader(fileReader);
            String[] newline;
            while ((newline = reader.readNext()) != null) {
                disease.add(newline);
            }
            reader.close();
            
        } catch (Exception e) {
            System.out.println(e.toString());
        }

    }

    public static String getDisease(ArrayList<String> symptomps){
        
        Map<String,FieldValue> args = new LinkedHashMap<>();
        List<? extends InputField> inputFields = evaluator.getInputFields();

        HashMap<String,Integer> inputDataRecord = new HashMap<>();

        for(Object col:jsonArray){
            if(symptomps.contains(col)){
                inputDataRecord.put(col.toString(), 1);
                continue;
            }
            inputDataRecord.put(col.toString(), 0);
        }

        
        for(InputField inputField: inputFields){
            Object rawValue = inputDataRecord.get(inputField.getName());
            
            FieldValue inputValue = inputField.prepare(rawValue);
            args.put(inputField.getName(), inputValue);
        }
        
        Map<String,?> results = evaluator.evaluate(args);
        List<? extends TargetField> targetFields = evaluator.getTargetFields();

        String[] targetValue = {};
        
        for(TargetField targetField: targetFields){
            String targetName = targetField.getName();
            
            targetValue = results.get(targetName).toString().split(",");
        }

        for(int i=0;i<targetValue.length;i++){
            if(String.valueOf(targetValue[i].split("=")[1]).equals("1.0")){
                return disease.get(Integer.parseInt(targetValue[i].split("=")[0].strip()))[1].toString();
            }
        }

        return "Please Retry";
    }
}
