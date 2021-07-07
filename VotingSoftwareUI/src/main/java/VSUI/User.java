package VSUI;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;




public class User {
    public enum sex{Male,Female}
    private int userId;
    private String fullname;
    private String emailAddress;
    private String password;
    private String phoneNumber;
    private sex sex;
    private String role="USER";
    private int vToken;
    private boolean isVerified=false;



    public User() {
    }

    public User(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User.sex getSex() {
        return sex;
    }

    public void setSex(User.sex sex) {
        this.sex = sex;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.password=bCryptPasswordEncoder.encode(password);
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getvToken() {
        return vToken;
    }

    public void setvToken(int vToken) {
        this.vToken = vToken;
    }
}
