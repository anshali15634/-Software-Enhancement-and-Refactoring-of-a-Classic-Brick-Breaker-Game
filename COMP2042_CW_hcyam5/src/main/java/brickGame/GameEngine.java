package brickGame;

public class GameEngine {

    private static GameEngine instance;

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    private boolean isStopped = true;
    private boolean paused = false;

    private GameEngine() {
        // Private constructor to prevent instantiation outside of this class.
    }

    public static synchronized GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }


    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }
    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    private synchronized void Update() {
        updateThread = new Thread(() -> {
            while (!updateThread.isInterrupted()) {
                try {
                    if (!paused) {
                        onAction.onUpdate();
                    }
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    return; // remove if error
                    //e.printStackTrace();
                }
            }
        });
        updateThread.start();
    }

    private void Initialize() {
        onAction.onInit();
    }

    private synchronized void PhysicsCalculation() {
        physicsThread = new Thread(() -> {
            while (!physicsThread.isInterrupted()) {
                try {
                    if (!paused) {
                        onAction.onPhysicsUpdate();
                    }
                    Thread.sleep(fps);
                } catch (InterruptedException e) {
                    return;
                    //e.printStackTrace();
                }
            }
        });

        physicsThread.start();

    }

    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }


    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }
    public void pause(){
        paused = true;
    }

    public void resume(){
        paused = false;
    }

    private long time = 0;

    private Thread timeThread;

    private void TimeStart() {
        timeThread = new Thread(() -> {
            try {
                while (true) {
                    time++;
                    onAction.onTime(time);
                    Thread.sleep(1);
                }
            } catch (InterruptedException e) {
                return;
                //e.printStackTrace();
            }
        });
        timeThread.start();
    }


    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }

}
