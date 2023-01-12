package sharedClasses;

import java.io.Serializable;
import javax.swing.*;

/**
 * This class represents a user which consists of a username (String) and a profile picture (ImageIcon).
 * It is an object class with its usual get functions and some comparison functions.
 */
public class User implements Serializable {
    private String userName;
    private ImageIcon image;

    /**
     * Constructor.
     * @param userName
     * @param image
     */
    public User(String userName, ImageIcon image) {
        this.userName = userName;
        this.image = image;
    }

    public String getUserName() {
        return userName;
    }

    public ImageIcon getImage() {
        return image;
    }

    public int hashCode() {
        return userName.hashCode();
    }

    public boolean equals(Object obj) {
        return userName.equals(((User)obj).userName);
    }
}

