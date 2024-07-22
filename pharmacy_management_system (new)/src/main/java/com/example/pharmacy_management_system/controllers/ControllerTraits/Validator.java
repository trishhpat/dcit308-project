package com.example.pharmacy_management_system.controllers.ControllerTraits;

import java.util.HashMap;

import javafx.scene.control.Label;

public class Validator{

    private static <T> HashMap<String,Object> mustBeText(T input) {
        HashMap<String,Object>  result = new HashMap<>();
        String data;
        String errorMessage = null;
        
        if (!(input instanceof String)) {
            errorMessage = "This field must be a text";
            data =null;
        }else{
            data = input.toString();
        }

        result.put("errorMessage",errorMessage);
        result.put("data",data);
        return result;
    }

    private static <T> HashMap<String,Object> required(T input) {
        HashMap<String,Object>  result = new HashMap<>();
        String data;
        String errorMessage = null;
        
        if ("".equals(input.toString())){
            errorMessage = "cannot be empty ";
            data =null;
        }else{
            data = input.toString();
        }

        result.put("errorMessage",errorMessage);
        result.put("data",data);
        return result;
    }

    
    private static <T> HashMap<String,Object> mustBeTextMoreThanCharacters(T input, int number) {
        HashMap<String,Object>  result = new HashMap<>();
        String errorMessage = null;
        String data=input.toString();

        if((data == null) || (data.length()<number)){
            errorMessage = "this feild must be more than " + number;
            data = null;
        }
        result.put("errorMessage",errorMessage);
        result.put("data",data);
        return result;
    }



    private static <T> HashMap<String,Object> mustBeTextLessThanCharacters(T input, int number) {
        HashMap<String,Object>  result = new HashMap<>();
        String errorMessage = null;
        String data=input.toString();

        if(data.length()>number){
            errorMessage = "this feild must be less than " + number;
            data = null;
        }


        result.put("errorMessage",errorMessage);
        result.put("data",data);
        return result;
    }

    private static <T> HashMap<String,Object> mustBeInteger(T input) {
        HashMap<String,Object>  result = new HashMap<>();
        String errorMessage = null;

        try {
           int data = Integer.parseInt(input.toString());
           result.put("data",data);
        } catch (Exception e) {
            errorMessage = "This field must be a Integer";
            result.put("data",null);
        }

        result.put("errorMessage",errorMessage);
        return result;
    }


    private static <T> HashMap<String,Object> mustBeFloat(T input) {
        HashMap<String,Object>  result = new HashMap<>();
        String errorMessage = null;

        try {
           float data = Float.parseFloat(input.toString());
           result.put("data",data);
        } catch (Exception e) {
            errorMessage = "This field must be a float";
            result.put("data",null);
        }

        result.put("errorMessage",errorMessage);
        return result;
    }


    private static  HashMap<String,Object> executeValidationPolicy(String methodText, String input){

        HashMap<String,Object>  result = new HashMap<>();

        int limit = 0 ;
        StringBuilder methodlimit = new StringBuilder();
        StringBuilder method = new StringBuilder();
        char separate = 'v';

        for (char c : methodText.toCharArray()) {
            if(c == '@'){separate = c;}
            else if(separate == '@'){methodlimit.append(c);}
            else{method.append(c);}
        }

        if(separate == '@'){
            try{
                limit = Integer.parseInt(methodlimit.toString());
            }catch (Exception e) {
                result.put("errorMessage","Invalid");
                result.put("data",null);
                return result;  
            }
        }
        if(method.toString().equals("mustBeText"))return mustBeText(input);
        if(method.toString().equals("required"))return required(input);
        if(method.toString().equals("mustBeTextMoreThanCharacters"))return mustBeTextMoreThanCharacters(input,limit);
        if(method.toString().equals("mustBeTextLessThanCharacters"))return mustBeTextLessThanCharacters(input,limit);
        if(method.toString().equals("mustBeFloat"))return mustBeFloat(input);
        if(method.toString().equals("mustBeInteger"))return mustBeInteger(input);
        result.put("errorMessage","Invalid");
        result.put("data",null);
        return result;    
    
    }

    private static void resetErrorFeilds(Label[] errorFeilds){
        if(errorFeilds.length!=0){
        for(Label label : errorFeilds){
            label.setText("");
        }};
    }

    public static boolean compareTwoFields(String password, String confirmPassword, Label displayErrorFeild){
        if(password.equals(confirmPassword)) return  true;
        else{
            displayErrorFeild.setText("passwords do not match");
            return false;
        }
    }

    public static boolean compareTwoFieldsAndClearFeild(String password, String confirmPassword, Label displayErrorFeild, Label[] errorFeilds){
        resetErrorFeilds(errorFeilds);
        if(password.equals(confirmPassword)) return  true;
        else{
            displayErrorFeild.setText("passwords do not match");
            return false;
        }
    }
    
    public static HashMap<String, HashMap<String, Object>>  validate(String[] fields, Label[] errorFeildLabels) {

        HashMap<String, HashMap<String, Object>>  validationResult = new HashMap<>();
        HashMap<String, Object> errorState = new HashMap<>();
        HashMap<String, Object> errorDetails = new HashMap<>();
        HashMap<String, Object> validatedData = new HashMap<>();

        resetErrorFeilds(errorFeildLabels);


        for (String field : fields) {
            String fieldName = "";
            String fxid = "";
            int labelIndex = -1;
            String input = "";
            String method = "";
            String methodErrors;
            Object validData =null;
            StringBuilder methodResult = new StringBuilder();

            char begin = 'v';
            char end = 'v';
            StringBuilder text = new StringBuilder();

            for (char c : field.toCharArray()) {
                if (c == '[') {
                    begin = '[';
                    text = new StringBuilder();
                } else if (c == ']') {
                    end = ']';
                    if (begin == '[' && end == ']') {
                        fieldName = text.toString();
                        begin = 'v'; end = 'v';
                    }
                } else if (c == '{') {
                    begin = '{';
                    text = new StringBuilder();
                } else if (c == '}') {
                    end = '}';
                    if (begin == '{' && end == '}') {
                        fxid = text.toString();
                        try{
                        labelIndex = Integer.parseInt(fxid);
                        }catch (Exception e){
                        System.out.println("check validations well");
                        }
                        begin = 'v'; end = 'v';
                    }
                } else if (c == '<') {
                    begin = '<';
                    text = new StringBuilder();
                } else if (c == '>') {
                    end = '>';
                    if (begin == '<' && end == '>') {
                        input = text.toString();
                        begin = 'v'; end = 'v';
                    }
                } else if (c == '(') {
                    begin = '(';
                    text = new StringBuilder();
                } else if (c == ')') {
                    end = ')';
                    if (begin == '(' && end == ')') {
                        method = text.toString();
                        HashMap<String,Object> executeMethod  = executeValidationPolicy(method, input);
                        if (executeMethod.get("errorMessage") != null) {
                            methodResult.append(executeMethod.get("errorMessage").toString());
                            errorFeildLabels[labelIndex].setText(executeMethod.get("errorMessage").toString()+" ");
                            methodResult.append(" ");
                        }else{
                            validData = executeMethod.get("data");
                        }
                        begin = 'v'; end = 'v';
                    }
                } else {
                    text.append(c);
                }
            }

            if (methodResult.length() == 0)
                methodErrors = "";
            else
                methodErrors = methodResult.toString();

            
            errorDetails.put(fxid,methodErrors);
            validatedData.put(fieldName,validData);        
        }




        for(Object value : errorDetails.values()){
            if(!"".equals(value)){
                errorState.put("isvalid",false);
                validatedData.clear();
                validationResult.put("errorState",errorState);
                validationResult.put("errorDetails",errorDetails);
                validationResult.put("validatedData",validatedData);
                return validationResult;
            } 
        }

        errorState.put("isvalid",true);
        errorDetails.clear();
        validationResult.put("errorState",errorState);
        validationResult.put("errorDetails",errorDetails);
        validationResult.put("validatedData",validatedData);

        return validationResult;
    }
}