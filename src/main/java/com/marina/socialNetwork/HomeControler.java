/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marina.socialNetwork;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Marina
 */
@Controller
public class HomeControler {
     // Go to index page and show all the users
    @RequestMapping(value={"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView showUsers() throws IOException, FileNotFoundException, ParseException{
       ModelAndView modelAndView = new ModelAndView();
       List <JSONObject> listUsers = UserService.listAllUsers();
       modelAndView.addObject("listusers", listUsers);
       modelAndView.setViewName("index");
       return modelAndView;
    }
     // When you click the button SEE FRIENDS on the list of all users on the index page
    
    @GetMapping("/users/friends")
    public String UsersFriends(Model model, @RequestParam(defaultValue="")  Long id) throws IOException, FileNotFoundException, ParseException {
            //take user's id and store into variable
            Long idUser = id-1;
            //find user's friends
            List <JSONObject> usersFriends = UserService.listFriends(idUser);
            //sends id and list of user's friends on page friendsList.html
            model.addAttribute("id", idUser);
            model.addAttribute("usersFriends", usersFriends);
            return "friendsList";
	}
    
    //When you click the button SEE FRIENDS in the user friends, same logic like in previous method
    
    @GetMapping("/users/friendsOfFriends")
    public String UsersFriendsOfFriends(Model model, @RequestParam(defaultValue="")  Long id) throws IOException, FileNotFoundException, ParseException {
            Long idUser = id-1;
            List <JSONObject> friendsOfFriends = UserService.listFriends(idUser);
            model.addAttribute("friendsOfFriends", friendsOfFriends);
            return "friendsOfFriends";
	}
    //When you click the button Suggested Friends
    
   @GetMapping("/users/suggestedFriends")
    public String UsersSuggestedFriends(Model model, @RequestParam(defaultValue="")  Long id) throws IOException, FileNotFoundException, ParseException {
            Long idUser = id;
            //just takes ids of friends from user we choose
            
            JSONObject user1 =  (JSONObject) UserService.findUser(idUser);
            ArrayList idsOfFriends = (ArrayList) user1.get("friends");
            
            //store all ids of friends of friends
            
            ArrayList allFriendsOfFriends = new ArrayList();
            
            for (Object friend: idsOfFriends) {
                JSONObject user =  (JSONObject) UserService.findUser((Long) friend-1);
                ArrayList idsFriendsofFriends = (ArrayList) user.get("friends");
                for (Object idsFriendofFriend : idsFriendsofFriends) {
                    allFriendsOfFriends.add(idsFriendofFriend);
                }
       }
            //Store duplicates of friends
            ArrayList duplicatedFriends = new ArrayList();
            
            //I use set beacuse using it makes it easier to take out duplicates
            Set<Object> helpArray = new HashSet<>();
           
            for (Object allFriendsOfFriend : allFriendsOfFriends) {
                if (helpArray.add(allFriendsOfFriend) == false) {
                    duplicatedFriends.add(allFriendsOfFriend);
                } }
            
            //remove duplicates
            
             duplicatedFriends = (ArrayList) duplicatedFriends.stream() 
                                      .distinct() 
                                      .collect(Collectors.toList());
            
             //Store all suggestedFriends
             
            List <JSONObject> suggestedFriends = new ArrayList <JSONObject>();
            for (Object duplicatedFriend : duplicatedFriends) {
            JSONObject newFriend = (JSONObject) UserService.findUser(((Long) duplicatedFriend)-1);
            suggestedFriends.add(newFriend);
        }
             
            model.addAttribute("suggestedFriends", suggestedFriends);
            return "suggestedFriends";}
        
}
