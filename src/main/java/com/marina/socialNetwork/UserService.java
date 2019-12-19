/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marina.socialNetwork;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author Marina
 */
public class UserService {
    static List<JSONObject> listUsers() throws FileNotFoundException, IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        ClassPathResource resource = new ClassPathResource("data.json");
        Object openJSON = jsonParser.parse(new FileReader(resource.getFile()));
        JSONArray listusers = (JSONArray) openJSON;
        return listusers;
    }
    static JSONObject findUser(Long id) throws FileNotFoundException, IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        ClassPathResource resource = new ClassPathResource("data.json");
        Object openJSON = jsonParser.parse(new FileReader(resource.getFile()));
        JSONArray listusers = (JSONArray) openJSON;
        JSONObject user = new JSONObject();
        int id1 = id.intValue();
        for(int i = 0; i<listusers.size();i++){
            if(listusers.get(i).equals(listusers.get(id1))){
            user = (JSONObject) listusers.get(id1);
            }
        }
        return user;
    }
}
