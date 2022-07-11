package com.dbest.sevs.Utils;


import com.dbest.sevs.entity.Poll;
import com.dbest.sevs.entity.User;
import org.springframework.web.client.RestTemplate;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmailAndSMS implements Runnable, Constants{
    private final String message;
    private final User user;
    private final String type;
    private Poll poll;

    private final String PASSWORD ="";

    private  final String FROM=""  ;

    private final String token ="";



    public EmailAndSMS(User user,String message,String type,Poll poll) throws IOException, GeneralSecurityException {
        this.user=user;
        this.message=message;
        this.type=type;
        this.poll=poll;
    }

    @Override
    public void run() {
        try {
            sendEmail();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                sendSMS();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void sendEmail() throws FileNotFoundException, URISyntaxException {
        generateText(EMAIL);
       logger("sending Email to",user.getEmailAddress(),Level.WARNING);
        try{
            mailSender();
        }
        catch (Exception e){
            logger("Email sending failed to",user.getEmailAddress(),Level.SEVERE);
            e.printStackTrace();
        }
    }

    private void mailSender() throws FileNotFoundException, MessagingException, URISyntaxException {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", 465);
        props.put("mail.smtp.user", FROM);

        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.debug", true);

        props.put("mail.smtp.socketFactory.port", 465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM, PASSWORD);
            }
        });

        //Construct the mail message
        MimeMessage message = new MimeMessage(session);
        message.setContent(generateText(EMAIL),"text/html");
        message.setSubject("SEVS "+type.replaceAll("_","\\ ").toUpperCase());
        message.setFrom(new InternetAddress(FROM));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmailAddress()));
        message.saveChanges();

        //Use Transport to deliver the message
        Transport transport = session.getTransport("smtp");
        transport.connect("smtp.gmail.com", FROM, PASSWORD);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }

    private void sendSMS() throws FileNotFoundException {
        logger("sending SMS to",user.getPhone(),Level.WARNING);
        try{
            new RestTemplate().getForObject("https://www.bulksmsnigeria.com/api/v1/sms/create?api_token="+token+"&from=SEVS&to="+user.getPhone()+"&body="+generateText(PHONE)+"&dnd=6",Object.class);
        }
        catch (Exception e){
            logger("SMS sending failed to",user.getEmailAddress(),Level.SEVERE);
            e.printStackTrace();
        }
    }
    private void logger(String message,String receiver,Level level){
        Logger.getLogger(EmailAndSMS.class.getName()).log(level,message+" "+receiver);
    }
    private String generateText(String medium) throws FileNotFoundException, URISyntaxException {
        if (PHONE.equals(medium)) {
            switch (type) {
                case RESET_PASSWORD:
                    return "Dear " + user.getFirstname() + ", your password reset was successful. your new password is " + message + ". please login to change it immediately";
                case TOKEN:
                    return "Dear " + user.getFirstname() + ", your token is " + message + ". " +(message.length()==4?" ":"This token is only valid for 5 minutes");
                case POLL_DECISION:
                    return "Dear " + user.getFirstname() + ", The winner of the poll " + poll.getTitle() + " is " + message;
                case ENROL:
                    return "You have been enrolled to participate in "+poll.getTitle()+" poll which opens and closes at "+
                             poll.getOpeningDate() +" "+poll.getClosingDate() +" respectively";
                default:
                    return "Dear " + user.getFirstname() + ", your request with reference-" + message + " was received and is being processed. Be sure to hear from us soon";
            }
        } else {
            String out;
            URL resource = getClass().getClassLoader().getResource("email.html");
            if (resource == null) {
                throw new IllegalArgumentException("file not found!");
            } else {
                out = new Scanner(new File(resource.toURI())).useDelimiter("\\A").next();
                out = out.replaceAll("-SALUTE-", "Dear " + user.getFirstname());
                out = out.replaceAll("-BODY3-", "Kind Regards</br> <p><b> Vote Smart</b></p>");
                switch (type) {
                    case RESET_PASSWORD:
                        out = out.replaceAll("-BODY-", "Your password reset was successful. your new password is");
                        out = out.replaceAll("-VALUE-", message);
                        out = out.replaceAll("-BODY2-", "please login to change it immediately");
                        return out;
                    case TOKEN:
                        out = out.replaceAll("-BODY-", "Your token is");
                        out = out.replaceAll("-VALUE-", message);
                        out = out.replaceAll("-BODY2-", message.length()==4?"":"This token is only valid for 5 minutes");
                        return out;
                    case POLL_DECISION:
                        out = out.replaceAll("-BODY-", "The winner of the poll "+poll.getTitle()+" which opened and closed at " +poll.getOpeningDate()+" and "+poll.getClosingDate()+" respectively is");
                        out = out.replaceAll("-VALUE-", message);
                        out = out.replaceAll("-BODY2-", "");
                        return out;
                    case ENROL:
                        out = out.replaceAll("-BODY-", "You have been enrolled to participate in "+poll.getTitle()+" poll which opens and closes at ");
                        out = out.replaceAll("-VALUE-", poll.getOpeningDate() +" and "+poll.getClosingDate() +" respectively");
                        out = out.replaceAll("-BODY2-", "");
                        return out;
                    default:
                        out = out.replaceAll("-BODY-", "Your request with reference-");
                        out = out.replaceAll("-VALUE-", message + " was received and is being processed");
                        out = out.replaceAll("-BODY2-", "Be sure to hear from us soon");
                        return out;
                }

            }
        }
    }
}
