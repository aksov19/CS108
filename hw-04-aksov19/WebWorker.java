import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WebWorker extends Thread {

    private final WebFrame.WorkerLauncher launcher;
    private final int index;

    /**
     * @param launcher WorkerLauncher that launched this worker
     * @param index Index of the link to process
     */
    public WebWorker(WebFrame.WorkerLauncher launcher, int index){
        this.launcher = launcher;
        this.index = index;
    }


    @Override
    public void run(){
        synchronized (launcher.webFrame.runningCount){ launcher.webFrame.runningCount++; }
        launcher.webFrame.updateInfoLabels();

        download();
    }


    private void download(){
        InputStream input = null;
        StringBuilder contents = null;
        String status = "err";
        try {
            long tick = System.currentTimeMillis();

            String link = launcher.webFrame.getLink(index);
            URL url = new URL(link);
            URLConnection connection = url.openConnection();

            // Set connect() to throw an IOException
            // if connection does not succeed in this many msecs.

            //
            if(launcher.interrupted) throw new InterruptedException();
            connection.setConnectTimeout(5000);
            if(launcher.interrupted) throw new InterruptedException();
            //

            connection.connect();
            input = connection.getInputStream();

            //
            if(launcher.interrupted) throw new InterruptedException();

            BufferedReader reader  = new BufferedReader(new InputStreamReader(input));

            char[] array = new char[1000];
            int len;
            contents = new StringBuilder(1000);
            while ((len = reader.read(array, 0, array.length)) > 0) {
                //
                if(launcher.interrupted) throw new InterruptedException();

                contents.append(array, 0, len);
                Thread.sleep(100);

                //
                if(launcher.interrupted) throw new InterruptedException();
            }

            // Successful download if we get here
            long tock = System.currentTimeMillis();
            long time = tock-tick;
            SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
            status = df.format(new Date()) + " " + time + "ms " + contents.length();

            // +1 completed count
            synchronized (launcher.webFrame.completedCount){ launcher.webFrame.completedCount++; }
        }
        // Otherwise control jumps to a catch...
        catch(MalformedURLException ignored) { }
        catch(InterruptedException exception) {
            // YOUR CODE HERE
            status = "Interrupted";
        }
        catch(IOException ignored) {}
        // "finally" clause, to close the input stream
        // in any case
        finally {
            try{
                if (input != null) input.close();
            }
            catch(IOException ignored) {}

            // Update status and display info
            launcher.webFrame.setStatus(index, status);
            synchronized (launcher.webFrame.runningCount){ launcher.webFrame.runningCount--; }
            launcher.webFrame.updateInfoLabels();
            launcher.workerLock.release();
        }
    }

}
