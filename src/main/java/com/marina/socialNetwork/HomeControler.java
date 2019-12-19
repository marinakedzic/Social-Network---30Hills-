/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marina.socialNetwork;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
       List <JSONObject> listUsers = UserService.listUsers();
       modelAndView.addObject("listusers", listUsers);
       modelAndView.setViewName("index");
       return modelAndView;
    }
    @GetMapping("/users/friends")
    public String UsersFriends(Model model, @RequestParam(defaultValue="")  Long id) throws IOException, FileNotFoundException, ParseException {
            Long idUser = id-1;
            JSONObject user =  (JSONObject) UserService.findUser(idUser);
            ArrayList friends = (ArrayList) user.get("friends");
            ArrayList friendsOfFriends = new ArrayList();
            for (Object friend : friends) {
            JSONObject newFriend = (JSONObject) UserService.findUser(((Long) friend)-1);
            friendsOfFriends.add(newFriend.get("friends"));
        }
            model.addAttribute("friendsOfFriends", friendsOfFriends);
            model.addAttribute("friends", friends);
            return "friendsList";
	}
}
