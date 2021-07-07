package VSUI.Controllers;

import VSUI.Contestants;
import VSUI.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@RestController
@CrossOrigin("http://localhost:4200")
public class VoteController {
    @Autowired
    RestTemplate restTemplate;
    String voteURI="http://vote-api";
    @RequestMapping(method = RequestMethod.GET,value = "/vote/regButton/{voteId}")
    public ResponseEntity<?> regButton(@PathVariable int voteId){
        return restTemplate.getForEntity(voteURI+"/reg/button/"+voteId,String.class);
    }
    @RequestMapping(method = RequestMethod.GET,value = "/vote/voteButton/{voteId}")
    public ResponseEntity<?> voteButton(@PathVariable int voteId){
        return restTemplate.getForEntity(voteURI+"/vote/button/"+voteId,String.class);
    }
    @RequestMapping(method = RequestMethod.POST,value = "/vote")
    public ResponseEntity<?> addVote(@RequestBody Vote vote){
        return restTemplate.postForEntity(voteURI+"/new/vote",vote,Void.class);
    }
    @RequestMapping(method = RequestMethod.GET,value = "/vote")
    public ResponseEntity<?> getAllVotes(){
        return new ResponseEntity<>(restTemplate.getForObject(voteURI+"/vote", List.class),HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.POST,value = "/vote/{voteId}/contestant")
    public ResponseEntity<?> addContestant(@RequestBody Contestants contestant, @PathVariable int voteId){
        return restTemplate.postForEntity(voteURI+"/new/"+voteId+"/contestant",contestant,Void.class);
    }
    @RequestMapping(method = RequestMethod.GET,value="/vote/{voteId}")
    public ResponseEntity<Vote> getVote(@PathVariable int voteId){
        Vote vote= restTemplate.getForObject(voteURI+"/"+voteId,Vote.class);
        return new ResponseEntity<>(vote, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.DELETE,value = "/vote/{voteId}")
    public void deleteContestant(@PathVariable int voteId){
         restTemplate.delete(voteURI+"/"+voteId);
    }
}
