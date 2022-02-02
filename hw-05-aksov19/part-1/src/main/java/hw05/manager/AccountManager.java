package hw05.manager;
import java.util.HashMap;

public class AccountManager {
    private final HashMap<String, String> accounts;

    public AccountManager(){
        accounts = new HashMap<>();
    }


    public boolean addAccount(String username, String password){
        if ( username == null || password == null ||
             username.length() == 0 || password.length() == 0 ||
             accounts.containsKey(username) ) return false;

        accounts.put(username, password);
        return true;
    }


    public boolean matchesPassword(String username, String password){
        if( username == null || password == null ||
            !accounts.containsKey(username) ) return false;

        return accounts.get(username).equals(password);
    }


    public boolean userExists(String username){
        return accounts.containsKey(username);
    }
}
