package hack.form;

public class ShoppingItem {
    private String url;
    private String userName;
    private String userEmail;



    public ShoppingItem(String userName, String userEmail, String url) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
