
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class GameTimer {

    public static Timer t;
    public static int delay = 1000; //milliseconds or 1 second
    public static GameFrame gf;
    public final AtomicInteger time;

    /**
     * Starts a new timer from a given startTime
     *
     * @param gf
     * @param startTime
     */
    public GameTimer(GameFrame gf, int startTime) {
        this.gf = gf;
        time = new AtomicInteger(startTime);
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                //  Event Dispatch Thread (AWT-EventQueue-0)
                gf.displayTime(formatTime(time.intValue()));
                time.getAndIncrement();
            }
        };
        t = new Timer(delay, taskPerformer);
    }

    /**
     * Starts a new game timer from 00:00:00
     *
     * @param gf
     */
    public GameTimer(GameFrame gf) {
        this(gf, 0);
    }

    /**
     * startTimer - begin or resumes the timer
     */
    public void startTimer() {
        t.start();
    }

    /**
     * stopTimer - stops or pauses the timer
     */
    public void stopTimer() {
        t.stop();
    }

    /**
     * getTime - returns the current time
     *
     * @return current time
     */
    public int getTime() {
        return time.intValue();
    }

    /**
     * formatTime - returns the time in the hours:minutes:seconds format
     *
     * @param seconds time in seconds
     * @return string of time in hr:min:sec
     */
    public String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int seconds_out = (seconds % 3600) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds_out);
    }

}
