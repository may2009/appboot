package com.example.demo.api;

import com.example.demo.dao.UserRepo;
import com.example.demo.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@CrossOrigin(origins = "*")
@RestController
public class UserApi {
    @Autowired
    UserRepo userRepo;


    @RequestMapping(value = "/getUser", method = RequestMethod.GET)
    public List<User> getUser(){
        return  userRepo.findAll();
    }
    

    @RequestMapping(value = "/getAllRestng", method = RequestMethod.POST)
    public ResponseEntity<DataTable> getAllRestng(@RequestBody Map<String, Object> payload){
        Map<String, Object> model = new HashMap<>();


        int start = (int) payload.get("start");
        int draw = (int) payload.get("draw");
        int length = (int) payload.get("length");
        HashMap<String, Object> search = (HashMap<String, Object>) payload.get("search");
        String s = (String) search.get("value");

        ArrayList<HashMap<String, String>> order = (ArrayList<HashMap<String, String>>) payload.get("order");
        ArrayList<HashMap<String, Integer>> order1 = (ArrayList<HashMap<String, Integer>>) payload.get("order");

        String dir = order.get(0).get("dir");
       Integer column = order1.get(0).get("column");

        String sort="id";
        if(column==1){sort="name";}
        if(column==2){sort="lastName";}
        if(column==3){sort="email";}

        int page = start / length;

        PageRequest  pageable = PageRequest.of(
                page,
                length,
                Sort.by(sort)
        );
        Page<User> responseData;

        if(s!=null && s!="")
        {
            responseData = userRepo.getSearch(s,pageable);
        }else{
            responseData = userRepo.getAll(pageable);
        }




        DataTable dataTable = new DataTable();

        dataTable.setData(responseData.getContent());
        dataTable.setRecordsTotal(responseData.getTotalElements());
        dataTable.setRecordsFiltered(responseData.getTotalElements());
        dataTable.setDraw(draw);
        dataTable.setStart(start);



        return ResponseEntity.ok(dataTable);

    }

    @PostMapping(value = "/createuser")
    public String createuser(@RequestBody User user){
        userRepo.save(user);
        return "Hi "+user.getName()+" registration successfully";
    }

    @PostMapping("/updateuser/{id}")
    public String updateuser(@RequestBody User user) {

        user.setName(user.getName());
        user.setLastname(user.getLastname());
        user.setEmail(user.getEmail());
        user.setPassword(user.getPassword());
        user.setConfirmpassword(user.getConfirmpassword());
        user.setRole(user.getRole());
        user.setAdress(user.getAdress());
        user.setBgcolor(user.getBgcolor());
        user.setFacebook(user.getFacebook());
        user.setTwitter(user.getTwitter());
        user.setGoogle(user.getGoogle());
        user.setPhone(user.getPhone());
        user.setImageurl(user.getImageurl());
        user.setSalaire(user.getSalaire());
        
        userRepo.save(user);
        return "Hi "+user.getName()+" User updated successfully.";
    }

    @RequestMapping(value = "/getAllRest", method = RequestMethod.GET)
    public List<User> getAll(){
        return  userRepo.findAll();
    }


    @PostMapping("/deleteUserRest/{id}")
    public void deleteUserRest(@PathVariable int id) throws JsonProcessingException {

            userRepo.deleteById(id);

    }
    @RequestMapping(value = "/GetOneUserRest/{id}", method = RequestMethod.GET)
    public User GetOneUserRest(@PathVariable int id){
        return  userRepo.getOneUser(id);
    }

    @RequestMapping(value = "/chackmail/{val}", method = RequestMethod.GET)
    public int chackmail(@PathVariable String val){
        return  userRepo.checkmail(val);
    }
    @RequestMapping(value = "/checkmailedit/{email}/{id}" , method = RequestMethod.GET)
    public int checkmailedit(@PathVariable String email,@PathVariable int id){
        return  userRepo.checkmailedit(email,id);
    }
}
