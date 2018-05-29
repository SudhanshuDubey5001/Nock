package zip5001.my.com.zip.data;

public class User {

    public String Email;
    public String Password;
    public String Message;
    public String userName;

    public User(String email, String password, String Message, String userName) {
        Email = email;
        Password = password;
        this.Message=Message;
        this.userName = userName;
    }
}
