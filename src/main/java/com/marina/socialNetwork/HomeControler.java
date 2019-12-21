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
    
    @RequestMapping(value={"/", "/index"}, method = RequestMethod.GET)
    public ModelAndView showUsers() throws IOException, FileNotFoundException, ParseException{
       ModelAndView modelAndView = new ModelAndView();
       List <JSONObject> listUsers = UserService.listAllUsers();
       modelAndView.addObject("listusers", listUsers);
       modelAndView.setViewName("index");
       return modelAndView;
    }
    @GetMapping("/users/friends")
    public String UsersFriends(Model model, @RequestParam(defaultValue="")  Long id) throws IOException, FileNotFoundException, ParseException {
            Long idUser = id-1;
            List <JSONObject> usersFriends = UserService.listUsers(idUser);
            model.addAttribute("id", idUser);
            model.addAttribute("usersFriends", usersFriends);
            return "friendsList";
	}
    @GetMapping("/users/friendsOfFriends")
    public String UsersFriendsOfFriends(Model model, @RequestParam(defaultValue="")  Long id) throws IOException, FileNotFoundException, ParseException {
            Long idUser = id-1;
            List <JSONObject> friendsOfFriends = UserService.listUsers(idUser);
            model.addAttribute("friendsOfFriends", friendsOfFriends);
            return "friendsOfFriends";
	}
   @GetMapping("/users/suggestedFriends")
    public String UsersSuggestedFriends(Model model, @RequestParam(defaultValue="")  Long id) throws IOException, FileNotFoundException, ParseException {
            Long idUser = id;
            JSONObject user1 =  (JSONObject) UserService.findUser(idUser);
            ArrayList allFriendsOfFriends = new ArrayList();
            ArrayList suggestedFriends = new ArrayList();
            Set<Object> helpArray = new HashSet<>();
            ArrayList friendsOfFriends1 = (ArrayList) user1.get("friends");
            for (Object friendsOfFriend : friendsOfFriends1) {
                JSONObject user =  (JSONObject) UserService.findUser((Long) friendsOfFriend-1);
                ArrayList tryThis = (ArrayList) user.get("friends");
                for (Object tryThi : tryThis) {
                    allFriendsOfFriends.add(tryThi);
                }
       }
           
            for (Object allFriendsOfFriend : allFriendsOfFriends) {
                if (helpArray.add(allFriendsOfFriend) == false) {
                    suggestedFriends.add(allFriendsOfFriend);
                } }
             suggestedFriends = (ArrayList) suggestedFriends.stream() 
                                      .distinct() 
                                      .collect(Collectors.toList());
            List <JSONObject> suggestedFriends1 = new ArrayList <JSONObject>();
            for (Object suggestedFriend : suggestedFriends) {
            JSONObject newFriend = (JSONObject) UserService.findUser(((Long) suggestedFriend)-1);
            suggestedFriends1.add(newFriend);
        }
             
            model.addAttribute("suggestedFriends", suggestedFriends1);
            return "suggestedFriends";}
        
}
