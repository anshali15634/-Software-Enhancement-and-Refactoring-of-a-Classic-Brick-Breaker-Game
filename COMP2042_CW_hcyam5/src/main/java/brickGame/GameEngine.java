package brickGame;
/**
 * This class represents the core game engine that manages the game loop, physics calculations, and time updates for the brick game application.
 */
public class GameEngine {

    private static GameEngine instance;

    private OnAction onAction;
    private int fps = 15;
    private Thread updateThread;
    private Thread physicsThread;
    private boolean isStopped = true;
    private boolean paused = false;

    /**
     * Private constructor to prevent instantiation outside of this class.
     */
    private GameEngine() {
        // Private constructor to prevent instantiation outside of this class.
    }
    /**
     * Gets the singleton instance of the GameEngine.
     * @return The GameEngine instance.
     */
    public static synchronized GameEngine getInstance() {
        if (instance == null) {
            instance = new GameEngine();
        }
        return instance;
    }

    /**
     * Sets the action listener that will be called for game events like updates, initialization, physics updates, and time updates.
     * @param onAction The action listener.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * Sets the frames per second (fps) of the game.
     * @param fps The desired fps (frames per second) in milliseconds
     */
    public void setFps(int fps) {
        this.fps = (int) 1000 / fps;
    }

    /**
     * Starts the game loop thread that updates the game state at a specified fps.
     */
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

    /**
     * Initializes the game by calling the onAction.onInit() method.
     */

    private void Initialize() {
        onAction.onInit();
    }

    /**
     * Starts the physics thread that performs physics updates at a specified fps.
     */
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
    /**
     * Starts the game engine by initializing the game, updating the game state, and starting the game loop threads for updates, physics, and time.
     */
    public void start() {
        time = 0;
        Initialize();
        Update();
        PhysicsCalculation();
        TimeStart();
        isStopped = false;
    }

    /**
     * Stops the game engine by interrupting the game loop, physics, and time threads.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            updateThread.interrupt();
            physicsThread.interrupt();
            timeThread.interrupt();
        }
    }
    /**
     * Pauses the game by setting the paused flag.
     */
    public void pause(){
        paused = true;
    }
    /**
     * Resumes the game by clearing the paused flag.
     */
    public void resume(){
        paused = false;
    }

    private long time = 0;

    private Thread timeThread;

    /**
     * Starts the time thread that updates the game time every millisecond.
     */
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
