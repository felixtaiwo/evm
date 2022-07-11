package com.dbest.sevs.Utils;

import com.dbest.sevs.repository.Poll;
import com.dbest.sevs.repository.Token;
import com.dbest.sevs.service.Logic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Component
public class ScheduledTasks {
    @Autowired
    Token token;
    @Autowired
    Logic service;
    @Autowired
    Poll poll;
    @Scheduled(cron = "0 */5 * * * *")
    public void deleteUsedToken(){
        try{
            for(com.dbest.sevs.entity.Token tok:token.findAll()){
                token.delete(tok);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    @Scheduled(cron = "0 */1 * * * *")
    public void openAndClosePolls() throws GeneralSecurityException, IOException, ClassNotFoundException {
        for(com.dbest.sevs.entity.Poll poll1:poll.findByIsClosed(false)){
            if(poll1.getClosingDate().before(new Date())){
                poll1.setClosed(true);
                poll.save(poll1);
                service.decideWinner(poll1.getId());
            }
        }
    }
}
