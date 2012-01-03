package org.knuth.multimediaremote.server.controller;

import org.knuth.multimediaremote.server.model.remotes.DetermineOS;
import org.knuth.multimediaremote.server.model.remotes.Remote;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Gets queried by the servers and performs the given
 *  action.
 */
public enum Controller {

    /** The instance to work with */
    INSTANCE;

    /**
     * Possible actions that the Controller does
     *  understand.
     */
    public enum Actions{
        PAUSE_PLAY, STOP,
        NEXT, PREVIOUS,
        VOL_UP, VOL_DOWN, MUTE;

        /** Not instantiable */
        private Actions(){
            poisoned = false;
        }
        /** Status of the poison-pill */
        private boolean poisoned;

        /**
         * Set the poison-pill to be taken.
         * @see <a href="http://stackoverflow.com/questions/812342">StackOverflow</a>
         */
        void interrupt(){
            poisoned = true;
        }

        /**
         * Indicates if the poison was taken and the queue
         *  should stop.
         * @return if the queue is poisoned.
         */
        boolean isInterrupted(){
            return poisoned;
        }
    }

    /** BlockingQueue to manage the incoming actions */
    private BlockingQueue<Actions> queue;

    /** The remote to execute the actions on */
    private Remote remote;

    /** Executor which runs the actions-queue */
    private ExecutorService queue_service;

    /**
     * Executes the first action from the queue
     */
    private Runnable execute_action = new Runnable() {
        /**
         * Wait on the queue to proceed and execute the
         *  given action.
         */
        public void run() {
            System.out.println("Starting to listen for Queue...");
            while (!queue_service.isShutdown()){
                try {
                    Actions action = queue.take();
                    // Check for poison-pill:
                    if (action.isInterrupted())
                        return;
                    // Execute action on Remote.
                    switch (action){    // TODO Looks a little ugly here. Maybe let the Remote do this?
                        case PAUSE_PLAY:
                            remote.pausePlay();
                            break;
                        case STOP:
                            remote.stop();
                            break;
                        case NEXT:
                            remote.nextTrack();
                            break;
                        case PREVIOUS:
                            remote.previousTrack();
                            break;
                        case VOL_UP:
                            remote.volUp();
                            break;
                        case VOL_DOWN:
                            remote.volDown();
                            break;
                        case MUTE:
                            remote.mute();
                            break;
                    }
                    System.out.println("Execute: "+action);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Stopping to listen for Queue...");
        }
    };

    /**
     * Initialize everything necessary and enforce the Singleton-
     *  pattern.
     */
    private Controller(){
        queue = new ArrayBlockingQueue<Actions>(3);
        // Determine OS and get the correct remote.
        remote = DetermineOS.getNativeRemote();
        // Start the queue:
        queue_service = Executors.newSingleThreadExecutor();
        queue_service.execute(execute_action);
    }

    /**
     * <b>MUST</b> be called when the application wants to shut
     *  down. This will stop the Controllers queue-thread and
     *  enable the program to fully shut down.
     */
    public void shutdown(){
        // Poison-Pill:
        Actions pill = Actions.MUTE;
        pill.interrupt();
        addAction(pill);
        // Shutdown gracefully:
        queue_service.shutdown();
    }

    /**
     * Add a new action to the queue.
     * <br>
     * It will be executed as soon as possible.
     * @param action The action to add.
     */
    public void addAction(Actions action){
        queue.add(action);
    }

}