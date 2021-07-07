package VSUI;

public class Message {
    private String phoneNumber;
    private String emailAddress;
    private String subject;
    private String messageBody;

    public Message() {
    }

    public Message(String phoneNumber, String emailAddress, String subject, String messageBody) {
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.subject = subject;
        this.messageBody = messageBody;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
