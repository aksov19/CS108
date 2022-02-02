import junit.framework.TestCase;

import java.util.Random;

public class CrackerTest extends TestCase {

    Random rand;

    protected void setUp() throws Exception{
        rand = new Random();
        super.setUp();
    }


    private String getRandomString(int len){
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            int index = rand.nextInt( Cracker.CHARS.length );
            sb.append( Cracker.CHARS[index] );
        }

        return sb.toString();
    }


    private final static int RANDOM_TEST_COUNT = 100;
    private final static int MAX_STR_LENGTH = 4;

    public void testRandomStrings(){
        String s;
        String encoded;
        Cracker cracker;
        int num;

        for(int i = 0; i < RANDOM_TEST_COUNT; i++){
            num = rand.nextInt(MAX_STR_LENGTH) + 1;
            s = getRandomString( num );
            try{
                encoded = Cracker.encodeString(s);
                cracker = new Cracker(encoded, num, 4);
                assertEquals(s, cracker.getDecodedString());

            } catch (Exception ignored) { }
        }
    }


    public void testMain(){
        try{
            Cracker.main(new String[]{"test"});
        }catch(Exception ignored){
            fail();
        }

        try{
            Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "2"});
        }catch(Exception ignored){
            fail();
        }

        try{
            Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "2", "2"});
        }catch(Exception ignored){
            fail();
        }

        try{
            Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "text"});
            fail();
        }catch(Exception ignored){}

        try{
            Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "2", "text"});
            fail();
        }catch(Exception ignored){}

        try{
            Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "2", "2", "garbage", "garbage"});
        }catch(Exception ignored){
            fail();
        }

        try{
            Cracker.main(new String[]{});
            fail();
        }catch(Exception ignored){
        }
    }
}
