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
//finds JSON file and makes list of all object in file
public class UserService {
    static List<JSONObject> listAllUsers() throws FileNotFoundException, IOException, ParseException {
        //JSONParser for reading JSON files
        JSONParser jsonParser = new JSONParser();
        //find JSON file in resources and open
        ClassPathResource resource = new ClassPathResource("data.json");
        Object openJSON = jsonParser.parse(new FileReader(resource.getFile()));
        //make list of all object in JSONArray from file
        JSONArray listOfUsers = (JSONArray) openJSON;
        return listOfUsers;
    }
    
    //finding specific user
    
    static JSONObject findUser(Long id) throws FileNotFoundException, IOException, ParseException {
        //find all users
        JSONArray listOfUsers = (JSONArray) listAllUsers();
        //make new JSONObject
        JSONObject user = new JSONObject();
        //takes id from user and converts it to int because arrays only work with int
        int id1 = id.intValue();
        //finding user with previous id in the list of all users
        for(int i = 0; i<listOfUsers.size();i++){
            if(listOfUsers.get(i).equals(listOfUsers.get(id1))){
            user = (JSONObject) listOfUsers.get(id1);
            }
        }
        return user;
    }
    
    //Finding all friends of specific user
    
    static List<JSONObject> listFriends(Long idUser) throws FileNotFoundException, IOException, ParseException {
            //find user
            JSONObject user =  (JSONObject) UserService.findUser(idUser);
            //store all friend's ids from user
            ArrayList friends = (ArrayList) user.get("friends");
            //make list of friends based on ids in previuos list
            List <JSONObject> usersFriends = new ArrayList <JSONObject>();
            for (Object friend : friends) {
            JSONObject newFriend = (JSONObject) UserService.findUser(((Long) friend)-1);
            usersFriends.add(newFriend);
        }
            return usersFriends;
    }
    
}
