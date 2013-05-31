package hack.form;

import java.net.URI;
import java.net.URISyntaxException;

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

    public String getDomain() {
        try {
            String httpUrl = url.startsWith("http") ? url : "http://" + url;
            URI uri = new URI(httpUrl);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
    }
}
