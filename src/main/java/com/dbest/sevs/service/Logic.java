package com.dbest.sevs.service;

import com.dbest.sevs.Utils.Constants;
import com.dbest.sevs.Utils.EmailAndSMS;
import com.dbest.sevs.Utils.HelperFunc;
import com.dbest.sevs.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Service
public class Logic implements Constants {
    @Autowired
    private Audit audit;
    @Autowired
    private BallotCard ballotCard;
    @Autowired
    private Candidate candidate;
    @Autowired
    private Poll poll;
    @Autowired
    private RecordTracker recordTracker;
    @Autowired
    private Token token;
    @Autowired
    private User user;
    @Value("${maxTrial}")
    private int maxTrial;
    @Autowired
    private Support support;
    @Value("${admin-u}")
    private String username;
    @Value("${admin-p}")
    private String password;


    HelperFunc helperFunc = new HelperFunc();

    private SealedObject encryptBallot(com.dbest.sevs.POJO.BallotCard card) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        return helperFunc.encryptObject(card);
    }

    private com.dbest.sevs.POJO.BallotCard decrptBallot(SealedObject sealedObject) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException, ClassNotFoundException {
        return (com.dbest.sevs.POJO.BallotCard) helperFunc.decryptObject(sealedObject);
    }

    private com.dbest.sevs.POJO.Token decrpytToken(SealedObject sealedObject) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException, ClassNotFoundException {
        return (com.dbest.sevs.POJO.Token) helperFunc.decryptObject(sealedObject);
    }

    private SealedObject encryptToken(com.dbest.sevs.POJO.Token token) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        return helperFunc.encryptObject(token);
    }

    public boolean confirmToken(com.dbest.sevs.entity.User user1, String token) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException, ClassNotFoundException {
        com.dbest.sevs.POJO.Token token1 = decrpytToken(user1.getToken().getSealedObject());
        user1.setTrialCount(user1.getTrialCount() + 1);
        boolean status = !token1.isExpired() && token1.getValue().equals(token);
        if (!status && user1.getTrialCount() == maxTrial) {
            user1.setBlocked(true);
        }
        if (status) {
            user1.setToken(null);
        }
        user.save(user1);
        return status;
    }

    public Date generateUserToken(com.dbest.sevs.entity.User user1, String type) throws GeneralSecurityException, IOException {
        com.dbest.sevs.POJO.Token userToken = new com.dbest.sevs.POJO.Token();
        userToken.setValue(helperFunc.generateToken(type));
        if (type.equalsIgnoreCase("confirmAccount")) {
            userToken.setExpires(new Date(new Date().getTime() + 1728000000));
        } else {
            userToken.setExpires(new Date(new Date().getTime() + 300000));
        }
        SealedObject tok = encryptToken(userToken);
        com.dbest.sevs.entity.Token token1 = new com.dbest.sevs.entity.Token();
        token1.setSealedObject(tok);
        token.save(token1);
        user1.setToken(token1);
        user1.setTrialCount(0);
        user1.setLastModified(new Date());
        user.save(user1);
        EmailAndSMS(user1, userToken.getValue(), TOKEN,null);
        return userToken.getExpires();
    }

    public String getToken(com.dbest.sevs.entity.User user) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, IOException, InvalidKeyException, ClassNotFoundException {
        return decrpytToken(user.getToken().getSealedObject()).getValue();
    }

    public void addAndEditUser(com.dbest.sevs.entity.User user1) throws GeneralSecurityException, IOException {
        if (user1.getDateCreated() == null) {
            if(user1.getIdentifier()==null||user1.getIdentifier().isEmpty()){
                user1.setIdentifier(helperFunc.generateRef());
            }
            user1.setDateCreated(new Date());
            user1.setPassword("");
            user1.setTrialCount(0);
            user1.setChangePassword(false);
            user.save(user1);
            generateUserToken(user1, "confirmAccount");
        } else {
            user1.setLastModified(new Date());
            com.dbest.sevs.entity.User user2 = user.getById(user1.getId());
            if (!user2.getEmailAddress().equalsIgnoreCase(user1.getEmailAddress()) || !user2.getPhone().equalsIgnoreCase(user1.getPhone())) {
                user1.setPassword("");
                user1.setTrialCount(0);
                user1.setChangePassword(false);
                generateUserToken(user1, "confirmAccount");
            }
            user.save(user1);
        }
    }

    public void deleteUser(long id) {
        user.deleteById(id);
    }

    public void createAndEditPoll(com.dbest.sevs.entity.Poll poll1) {
        if (poll1.getDateModified() == null) {
            poll1.setDateCreated(new Date());
        } else {
            poll1.setDateModified(new Date());
        }
        poll.save(poll1);
    }

    public void addCandidate(com.dbest.sevs.entity.Candidate candidate1, long pollId) {
        if (candidate1.getLastModified() == null) {
            candidate1.setDateCreated(new Date());
            candidate1.setPoll(new com.dbest.sevs.entity.Poll(pollId));
        } else {
            candidate1.setLastModified(new Date());
        }

        candidate.save(candidate1);
    }

    public ResponseEntity<?> castVote(long pollId, long contestantId, long userId, String token) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, IOException, InvalidKeyException, BadPaddingException, ClassNotFoundException {
        com.dbest.sevs.entity.User user1 = user.getById(userId);
        com.dbest.sevs.entity.Poll poll1 = poll.getById(pollId);
        for (com.dbest.sevs.entity.RecordTracker rt : findByUser(user1)) {
            if (rt.getPoll().getId() == pollId)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Voted Already");
        }
        if (poll1.getOpeningDate().after(new Date()) || poll1.isClosed()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if (confirmToken(user1, token)) {
            user1.setToken(null);
            com.dbest.sevs.POJO.BallotCard card = new com.dbest.sevs.POJO.BallotCard(true);
            card.setContestantId(contestantId);
            card.setDate(new Date());
            SealedObject sealedObject = encryptBallot(card);
            com.dbest.sevs.entity.BallotCard bCard = new com.dbest.sevs.entity.BallotCard();
            bCard.setSealedObject(sealedObject);
            bCard.setPoll(poll1);
            ballotCard.save(bCard);
            com.dbest.sevs.entity.RecordTracker recordTracker1 = new com.dbest.sevs.entity.RecordTracker();
            recordTracker1.setHasVoted(true);
            recordTracker1.setDate(new Date());
            recordTracker1.setUser(user1);
            recordTracker1.setPoll(poll1);
            recordTracker.save(recordTracker1);
            user.save(user1);
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or Expired Token");
    }

    public void decideWinner(long pollId) throws GeneralSecurityException, IOException, ClassNotFoundException {
        long maxVoteCount = 0;
        boolean undecided = false;
        com.dbest.sevs.entity.Candidate winner = null;
        com.dbest.sevs.entity.Poll poll1 = poll.findById(pollId).get();
        List<com.dbest.sevs.entity.BallotCard> cards = ballotCard.findByPoll(poll1);
        for (com.dbest.sevs.entity.BallotCard card : cards) {
            com.dbest.sevs.POJO.BallotCard decryptedCard = decrptBallot(card.getSealedObject());
            incrementCount(decryptedCard.getContestantId());
        }
        poll1 = poll.findById(pollId).get();
        for (com.dbest.sevs.entity.Candidate candidate1 : poll1.getCandidates()) {
            if (candidate1.getVoteCount() > maxVoteCount) {
                winner = candidate1;
                maxVoteCount=candidate1.getVoteCount();
                undecided = false;
            }
            if (candidate1.getVoteCount() == maxVoteCount && winner!=candidate1) {
                undecided = true;
            }
        }
        if (!undecided) {
            poll1.setWinner(winner);
            poll.save(poll1);
        }
        for (com.dbest.sevs.entity.User user1 : findUsers(poll1)) {
            EmailAndSMS(user1, winner != null ? winner.getFirstname() + " " + winner.getLastname() : "undecided", POLL_DECISION,poll1);
        }
    }

    private void incrementCount(long contestantId) {
        com.dbest.sevs.entity.Candidate candidate1 = candidate.findById(contestantId).get();
        candidate1.setVoteCount(candidate1.getVoteCount() + 1);
        candidate.save(candidate1);
    }

    public com.dbest.sevs.entity.User findUserByEmailAddress(String email) {
        return user.findByEmailAddress(email);
    }

    public com.dbest.sevs.entity.User findUserByPhone(String phone) {
        return user.findByPhone(phone);
    }

    public void saveAudit(com.dbest.sevs.entity.Audit audit1) {
        audit.save(audit1);
    }

    public com.dbest.sevs.entity.Poll findPoll(long pollId) {
        return poll.getById(pollId);
    }

    public void accreditUser(long pollId, long userId) {
        try {
            com.dbest.sevs.entity.User user1 = user.getById(userId);
            com.dbest.sevs.entity.Poll poll1 = poll.getById(pollId);
            List<com.dbest.sevs.entity.User> users = poll1.getUsers();
            if (poll1.getOpeningDate().before(new Date())) {
                throw new Exception("Voting already commenced");
            }
            if (!users.contains(user1)) {
                users.add(user1);
                poll1.setUsers(users);
                poll.save(poll1);
                EmailAndSMS(user1,"",ENROL,poll1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File writeUsersToCSV() {
        File csvOutput = new File("REG_USERS.csv");
        long count = 0;
        try {
            FileWriter pw = new FileWriter(csvOutput);
            pw.write("SN,ID,FIRSTNAME,LASTNAME,EMAIL,PHONE\n");
            for (com.dbest.sevs.entity.User u : user.findAll()) {
                if (!u.isBlocked()) {
                    count++;
                    pw.write(csvWriter(u, count));
                }
            }
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csvOutput;
    }

    private String csvWriter(com.dbest.sevs.entity.User currUser, long count) {
        return count + "," + currUser.getId() + "," + currUser.getFirstname() + "," + currUser.getLastname() + "," + currUser.getEmailAddress() + "," + currUser.getPhone() + "\n";
    }

    public Date resendToken(String email) throws GeneralSecurityException, IOException, ClassNotFoundException {
        com.dbest.sevs.entity.User user1 = findUserByEmailAddress(email);
        try {
            com.dbest.sevs.POJO.Token token = decrpytToken(user1.getToken().getSealedObject());
            int size = token.getValue().length();
            if (!token.isExpired()) {
                EmailAndSMS(user1, token.getValue(), TOKEN,null);
            } else {
                generateUserToken(user1, size == 4 ? "confirmAccount" : "others");
            }
            return token.getExpires();
        } catch (Exception e) {
            return generateUserToken(user1, "others");
        }
    }

    private void EmailAndSMS(com.dbest.sevs.entity.User aimedUser, String message, String type, com.dbest.sevs.entity.Poll poll1) throws GeneralSecurityException, IOException {
        EmailAndSMS es = new EmailAndSMS(aimedUser, message, type,poll1);
        Thread thread = new Thread(es);
        thread.start();
    }

    public String resetPassword(com.dbest.sevs.entity.User user) throws GeneralSecurityException, IOException {
        String password = helperFunc.generatePassword();
        EmailAndSMS(user, password, RESET_PASSWORD,null);
        return password;
    }

    /**
     * support
     **/
    public void addOrEditSupport(com.dbest.sevs.entity.Support support1) throws GeneralSecurityException, IOException {
        if (support1.getDateRaised() == null) {
            support1.setDateRaised(new Date());
            support1.setStatus(false);
            support1.setReference(helperFunc.generateRef());
        } else {
            support1.setLastModified(new Date());
        }
        support.save(support1);
        EmailAndSMS(new com.dbest.sevs.entity.User(support1.getEmailAddress(), support1.getPhone()), support1.getReference(), SUPPORT,null);
    }

    public List<com.dbest.sevs.entity.Poll> findPolls(long userId) {
        return poll.findByUser(userId);
    }

    public List<com.dbest.sevs.entity.Poll> findAllPolls() {
        return poll.findAll();
    }

    public List<com.dbest.sevs.entity.RecordTracker> findByUser(com.dbest.sevs.entity.User user1) {
        return recordTracker.findByUser(user1);
    }
    public List<com.dbest.sevs.entity.User> findUsers(com.dbest.sevs.entity.Poll poll1) {
        return user.findUsers(poll1.getId());
    }

    public String handleAdminAuth() {
        return Base64.getEncoder().encodeToString((username+":"+password).getBytes(StandardCharsets.UTF_8));
    }

    public List<com.dbest.sevs.entity.User> getUsers(boolean status) {
        return user.findByBlocked(status);
    }
}
