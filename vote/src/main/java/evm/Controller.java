package evm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class Controller {
    @Autowired
    Service service;
    @RequestMapping(method = RequestMethod.POST,value = "/new/vote")
    public void createVote(@RequestBody Vote vote){
        service.newVote(vote);
    }
    @RequestMapping(method = RequestMethod.POST,value = "/new/{voteId}/contestant")
    public void createContestant(@RequestBody Contestants contestant,@PathVariable int voteId){
        contestant.setVote(new Vote(voteId,"",null));
        service.newContestant(contestant);
    }
    @RequestMapping(method=RequestMethod.GET,value = "/contestant/{cid}")
    public Contestants getContestant(@PathVariable int cid){
        return service.getContestant(cid);

    }
    @RequestMapping(method = RequestMethod.GET,value = "/reg/button/{voteId}")
    public String regButton(@PathVariable int voteId){
        Vote vote = service.getVote(voteId);
        if(vote.isRegistrationON()){
            vote.setRegistrationON(false);
            service.newVote(vote);
            return "Registration closed";
        } else{
            vote.setRegistrationON(true);
            service.newVote(vote);
            return "Registration is On";}
    }
    @RequestMapping(method = RequestMethod.GET,value = "/vote/button/{voteId}")
    public String voteButton(@PathVariable int voteId){
        Vote vote = service.getVote(voteId);
        if(vote.isVoteON()){
            vote.setVoteON(false);
            service.newVote(vote);
            service.getVote(voteId)
                    .getContestants()
                    .forEach(t->
                            {
                                t.setVoteCount(service.voteCount(t));
                               service.newContestant(t);
                            }
                    );
            winnerMatch(voteId);
            return "Voting closed";
        } else{
            vote.setVoteON(true);
            service.newVote(vote);
            regButton(voteId);
            return "Voting is On";}

    }
    @RequestMapping(method = RequestMethod.GET,value = "/{voteId}")
    public Vote getVote (@PathVariable int voteId){
        return service.getVote(voteId);
    }
    @RequestMapping(method=RequestMethod.GET,value = "/vote")
    public List<Vote> getAll(){
        return service.getAllVotes();
    }
    @RequestMapping(method = RequestMethod.DELETE,value = "/{cid}")
    public void deleteContestant(@PathVariable int cid){
        service.deleteContestant(cid);
    }

    public void winnerMatch(int voteId){
        int max_count=0;
        Contestants winner=null;
        List<Contestants> winners=new ArrayList<>();
        Vote vote = service.getVote(voteId);
        List<Contestants> contestantz=vote.getContestants();
        for(Contestants x:contestantz){
            if(x.getVoteCount()>max_count){
                max_count=x.getVoteCount();
                winner=x;
            }
        }
        for(Contestants x:contestantz){
            if(x.getVoteCount()==max_count){
                winners.add(x);
            }
        }

        if(winners.size()==1){
            vote.setWinner(winners.get(0));
            service.newVote(vote);
        }


    }
    @RequestMapping(method = RequestMethod.GET,value = "/ballot/{cid}")
    public void ballot(@PathVariable int cid){
        Ballot ballot = new Ballot(cid);
        service.ballot(ballot);
    }




}
