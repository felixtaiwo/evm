package VSUI.Controllers;

import VSUI.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;



@RestController
@CrossOrigin("http://localhost:4200")
public class UserController {

    @Autowired
    RestTemplate restTemplate;
    String userURI="http://user-api/api/v1/user";
    @RequestMapping(method = RequestMethod.POST,value = "/user")
    public ResponseEntity<String> addUser(@RequestBody User user){
        restTemplate.postForObject(userURI,user,Void.class);
        return new ResponseEntity<>("ok",HttpStatus.OK);
    }
//    @RequestMapping(method = RequestMethod.GET,value = "/user/all")
//    public ResponseEntity<?> getAllUser(){
//        return new ResponseEntity<>(restTemplate.getForObject(userURI+"/", Page.class),HttpStatus.OK);
//    }
    @RequestMapping(method = RequestMethod.POST,value = "/user/update/{emailAddress}")
    public ResponseEntity<String> updateUser (@PathVariable String emailAddress, @RequestBody User user){
        restTemplate.postForObject(userURI+"/update/"+emailAddress,user,Void.class);
        return new ResponseEntity<>("ok",HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET,value="/user/count")
    public int getCount(){
        return restTemplate.getForObject(userURI+"/number",Integer.class);
    }
    @RequestMapping(method = RequestMethod.GET,value="/user/{emailAddress}")
    public User getUser(@PathVariable String emailAddress){
        return restTemplate.getForObject(userURI+"/"+emailAddress,User.class);
    }
    @RequestMapping(method = RequestMethod.DELETE,value = "/user")
    public ResponseEntity<?> deleteAllUsers(){
        restTemplate.delete(userURI);
        return new ResponseEntity<>("ok",HttpStatus.OK);

    }
    @RequestMapping(method = RequestMethod.DELETE,value = "/user/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable int userId){
        restTemplate.delete(userURI+"/"+userId);
        return new ResponseEntity<>("ok",HttpStatus.OK);

    }
    @RequestMapping(method=RequestMethod.POST,value="/user/verify/{userId}/{token}")
    public ResponseEntity<?> verifyUser(@PathVariable int userId,@PathVariable int token){
        return restTemplate.postForEntity(userURI+"/"+userId+"/"+token,null,Boolean.class);

    }
    @RequestMapping(method = RequestMethod.GET,value="/user/resendToken/{userId}")
    public void resendVerificationToken(@PathVariable int userId){
        restTemplate.getForObject(userURI+"/token/"+userId,Void.class);
    }
    @RequestMapping(method = RequestMethod.POST,value = "/user/changePassword/{emailAddress}/{oldPassword}")
    public ResponseEntity<?> passwordChange(@PathVariable String emailAddress, @PathVariable String oldPassword, @RequestBody String newPassword){
        return restTemplate.postForEntity(userURI+"/changePassword/"+emailAddress+"/"+oldPassword,newPassword,Boolean.class);
    }
    @RequestMapping(method = RequestMethod.GET,value = "/user/{emailAddress}/password/reset")
    public ResponseEntity<?> resetPassword(@PathVariable String emailAddress){
        return restTemplate.getForEntity(userURI+"/resetPassword/"+emailAddress,String.class);
    }
    public ResponseEntity<?> finalReset(@PathVariable String emailAddress, @RequestBody String password){
        return restTemplate.postForEntity(userURI+"/resetPasswordFinal/"+emailAddress,password,Void.class);
    }
}
