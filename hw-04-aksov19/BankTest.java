import junit.framework.TestCase;

import java.util.Arrays;

public class BankTest extends TestCase {

    protected void setUp() throws Exception{
        super.setUp();
    }

    private final static String SMALL = "small.txt";
    private final static String MEDIUM = "5k.txt";
    private final static String LARGE = "100k.txt";


    public void testSmall(){
        try {
            Bank oneThread = new Bank(SMALL, 1);
            Bank twoThread = new Bank(SMALL, 2);
            Bank fourThread = new Bank(SMALL, 4);
            Bank tenThread = new Bank(SMALL, 10);

            for (int i = 0; i < oneThread.getAccounts().size(); i++) {
                assertEquals(oneThread.getAccounts().get(i), twoThread.getAccounts().get(i));
                assertEquals(oneThread.getAccounts().get(i), fourThread.getAccounts().get(i));
                assertEquals(oneThread.getAccounts().get(i), tenThread.getAccounts().get(i));
            }
        }
        catch (Exception ignored){
            fail();
        }
    }


    public void testMedium(){
        try {
            Bank oneThread = new Bank(MEDIUM, 1);
            Bank twoThread = new Bank(MEDIUM, 2);
            Bank fourThread = new Bank(MEDIUM, 4);
            Bank tenThread = new Bank(MEDIUM, 10);

            for (int i = 0; i < oneThread.getAccounts().size(); i++) {
                assertEquals(oneThread.getAccounts().get(i), twoThread.getAccounts().get(i));
                assertEquals(oneThread.getAccounts().get(i), fourThread.getAccounts().get(i));
                assertEquals(oneThread.getAccounts().get(i), tenThread.getAccounts().get(i));
            }
        }
        catch (Exception ignored){
            fail();
        }
    }


    public void testLarge(){
        try {
            Bank oneThread = new Bank(LARGE, 1);
            Bank twoThread = new Bank(LARGE, 2);
            Bank fourThread = new Bank(LARGE, 4);
            Bank tenThread = new Bank(LARGE, 10);

            for (int i = 0; i < oneThread.getAccounts().size(); i++) {
                assertEquals(oneThread.getAccounts().get(i), twoThread.getAccounts().get(i));
                assertEquals(oneThread.getAccounts().get(i), fourThread.getAccounts().get(i));
                assertEquals(oneThread.getAccounts().get(i), tenThread.getAccounts().get(i));
            }
        }
        catch (Exception ignored){
            fail();
        }
    }


    public void testMain(){
        try {
            Bank.main(new String[]{"garbage", "garbage"});
            fail();
        }catch (Exception ignored){}

        try {
            Bank.main(new String[]{"small.txt", "hello"});
            fail();
        }catch (Exception ignored){}

        try {
            Bank.main(new String[]{"small.txt", "1"});
        }catch (Exception ignored){
            fail();
        }

        try {
            Bank.main(new String[]{"small.txt", "1", "garbage", "garbage", "garbage"});
        }catch (Exception ignored){
            fail();
        }

        try {
            Bank.main(new String[]{"small.txt"});
        }catch (Exception ignored){
            fail();
        }

        try {
            Bank.main(new String[]{});
            fail();
        }catch (Exception ignored){}

        try{
            Bank.main(new String[]{"dummy.txt", "2"});
            fail();
        }catch (Exception ignored){}
    }
}
