import hw05.manager.AccountManager;
import junit.framework.TestCase;

public class AccountManagerTest extends TestCase {
    AccountManager manager;

    protected void setUp() throws Exception {
        super.setUp();
        manager = new AccountManager();
    }

    // Test for null and non-null values
    public void testAdd(){
        assertTrue( manager.addAccount("name", "pass") );
        assertTrue( manager.addAccount("name1", "pass1") );
        assertTrue( manager.addAccount("name2", "pass") );
        assertTrue( manager.addAccount("name3", "pass2") );

        assertFalse( manager.addAccount("name", "pass") );

        assertFalse( manager.addAccount("", "pass") );
        assertFalse( manager.addAccount("name5", "") );
        assertFalse( manager.addAccount("", "") );

        assertFalse( manager.addAccount("name6", null) );
        assertFalse( manager.addAccount(null, "pass") );
        assertFalse( manager.addAccount(null, null) );
    }


    public void testMatch(){
        assertFalse( manager.matchesPassword("name", "pass") );
        assertFalse( manager.matchesPassword("name", null) );
        assertFalse( manager.matchesPassword(null, "pass") );
        assertFalse( manager.matchesPassword(null, null) );

        manager.addAccount("name1", "pass1");
        manager.addAccount("name2", "pass2");
        manager.addAccount("name3", "pass3");
        manager.addAccount("name4", "pass1");

        assertTrue( manager.matchesPassword("name1", "pass1") );
        assertTrue( manager.matchesPassword("name2", "pass2") );
        assertTrue( manager.matchesPassword("name3", "pass3") );
        assertTrue( manager.matchesPassword("name4", "pass1") );

        assertFalse( manager.matchesPassword("name1", "pass100") );
        assertFalse( manager.matchesPassword("name2", "pass100") );
        assertFalse( manager.matchesPassword("name3", "pass100") );
        assertFalse( manager.matchesPassword("name4", "pass100") );

        assertFalse( manager.matchesPassword("name1", null) );
        assertFalse( manager.matchesPassword(null, "pass1") );
        assertFalse( manager.matchesPassword(null, null) );
    }


    public void testExists(){
        assertFalse( manager.userExists(null) );
        assertFalse( manager.userExists("user") );

        manager.addAccount("name1", "pass1");
        manager.addAccount("name2", "pass2");
        manager.addAccount("name3", "pass3");
        manager.addAccount("name4", "pass1");

        assertTrue( manager.userExists("name1") );
        assertTrue( manager.userExists("name2") );
        assertTrue( manager.userExists("name3") );
        assertTrue( manager.userExists("name4") );

        assertFalse( manager.userExists(null) );
        assertFalse( manager.userExists("name5") );
    }
}
