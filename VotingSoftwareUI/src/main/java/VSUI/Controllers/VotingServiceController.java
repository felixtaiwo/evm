package VSUI.Controllers;

import VSUI.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin("http://localhost:4200")
public class VotingServiceController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private Service service;
    Random random = new Random();
    @RequestMapping(value = "/register/{voteId}/{userId}",method = RequestMethod.GET)
    public ResponseEntity<?> register (@PathVariable int userId, @PathVariable int voteId){
        Vote vote=restTemplate.getForObject("http://vote-api/"+voteId, Vote.class);
        assert vote != null;
        if(vote.isRegistrationON()){
            List<VotingService> votingServiceX=service.findByUserId(userId);
            for(VotingService t:votingServiceX){
                if(t.getVoteId()==voteId){
                    return new ResponseEntity<>("You are already registered for this",HttpStatus.BAD_REQUEST);
                }
            }
            VotingService votingService = new VotingService();
            votingService.setUserId(userId);
            votingService.setVoteId(voteId);
            service.add(votingService);
            return new ResponseEntity<>("Registration Successful",HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Registration is not ON",HttpStatus.BAD_REQUEST);
        }

    }
    @RequestMapping(method=RequestMethod.GET,value = "/voteNow/{voteId}/{userId}")
    public  int validateVoter(@PathVariable int userId, @PathVariable int voteId) {
        int ret=-1;
        List<VotingService> list = service.findByUserId(userId);
        for (VotingService t : list) {
            if (t.getVoteId() == voteId && t.isAccredited() & !t.isHasVoted()) {
                Message message = new Message();
                User user=restTemplate.getForObject("http://user-api/api/v1/user/userId/"+userId,User.class);
                int pin = random.nextInt(899999) + 100000;
                message.setMessageBody("Your voting OTP is  "+pin);
                assert user != null;
                message.setEmailAddress(user.getEmailAddress());
                message.setPhoneNumber(user.getPhoneNumber());
                message.setSubject("Voting OTP");
                restTemplate.postForObject("http://message-api",message,String.class);
                t.setAuthCode(pin);
                t.setAuthExp(LocalTime.now().plusMinutes(3));
                System.out.println(t.getAuthExp());
                service.add(t);
                 ret= t.getId();
                 break;
            }
        }

        return ret;
    }
    @RequestMapping("/vote/{vsId}/{cid}")
    public ResponseEntity<?> voteNow(@PathVariable int vsId, @PathVariable int cid,@RequestBody int pin){
        if(vsId!=-1){
            VotingService serve=service.getVotingService(vsId);
            if(pin==serve.getAuthCode()&&serve.getAuthExp().isAfter(LocalTime.now())){
                serve.setHasVoted(true);
                serve.setVoteDate(LocalDateTime.now());
                restTemplate.getForObject("http://vote-api/ballot/"+cid,Void.class);
                service.add(serve);
                return new ResponseEntity<>("Voting Successful",HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Wrong or Expired pin",HttpStatus.BAD_REQUEST);
            }
        } else
            return new ResponseEntity<>("you are not accredited",HttpStatus.BAD_REQUEST);
    }
    @RequestMapping(value = "/accredit/{voteId}/{userId}",method = RequestMethod.GET)
    public ResponseEntity<?> accreditUser(@PathVariable int userId,@PathVariable int voteId){
        List<VotingService> list = service.findByUserId(userId);
        for (VotingService t : list) {
            if (t.getVoteId() == voteId) {
                t.setAccredited(true);
                service.add(t);
                break;
            }
        }
        return new ResponseEntity<>("Successfully Accredited",HttpStatus.OK);
    }
    @RequestMapping(value = "/getAll/{voteId}",method = RequestMethod.GET)
    public List<User> findByVoteId (@PathVariable int voteId){
        List<User> user=new ArrayList<>();
         service.findByVoteId(voteId).forEach(t->
            user.add(restTemplate.getForObject("http://user-api/api/v1/user/userId/"+t.getUserId(),User.class))
        );
         return user;
    }
}
