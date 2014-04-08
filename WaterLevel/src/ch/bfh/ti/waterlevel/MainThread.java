package ch.bfh.ti.waterlevel;

import android.graphics.Canvas;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.SurfaceHolder;


/**
 * model class for the water-level app
 * @author grosi
 * 
 * note: this class isn't perfect! For normal functionality an HandlerThread
 * without the "main" thread should be enough. But for teaching aspect this 
 * examples shows two different possibilities to implement asynchrony cycle
 * threads. 
 * 
 * thanks to mysecretroom.com for the great android graphic tutorial!
 */

public final class MainThread implements Runnable {
	
	/* constants */
	private final static String TAG = MainThread.class.getSimpleName();
	private final static int BUTTON_REFRESH_TIME = 50; /* 20Hz */
	private final static int ACCELERATOR_REFRESH_TIME = 50; /* 50 Hz */
	private final static int MAX_FPS = 50; /* desired FPS */
	private final static int MAX_FRAME_SKIPS = 5; /* maximum number of frames to be skipped */
	private final static int FRAME_PERIOD = 1000 / MAX_FPS; /* the frame period */
	
	/* model parameters */
	private HandlerThread myHandlerThread = null;
	private Thread myThread = null;
	private volatile boolean myDone = false;
	
	/* reference to UI thread */
	private static  BubbleView myView;
	
	/* handlers */
	private Handler myButtonHandler;
	private Handler myAcceleratorHandler;
	static private SurfaceHolder myHolder = null;
	
	/* hardware in and outputs */
	private LEDsButtons myLEDsButtons;
	private MotionSensor mySensor;
	
	private Vector3D myRotation;
	private boolean last_btn_value = false;
	
	/* change listener */
	private AcceleratorChangeListener myAcceleratorChangeListener;
	
	
	/**
	 * simple listener interface
	 * @author grosi
	 *
	 */
	public interface AcceleratorChangeListener {
		void onAcceleratorChange(int angle);
	}
	
	
	/**
	 * set a thread listener -> a previous listener will overwritten
	 * @param listener
	 */
	public void setAcceleratorChangeListener(AcceleratorChangeListener listener) {
		myAcceleratorChangeListener = listener;
	}
	
	
	/**
	 * notify the listener
	 */
	private void notifyListener() {
		if(myAcceleratorChangeListener != null) {
			synchronized (myRotation) {
				
				/* Convert to degrees and invert rotation */
				int angle_converted = (int)(myRotation.x * -180/Math.PI);
				
				/* Set limits */
				if(angle_converted < -45) {
					angle_converted = -45;
				}
				if(angle_converted > 45) {
					angle_converted = 45;
				}
				
				if(myLEDsButtons != null) {
					/* Handle LEDs */
					if(angle_converted < 10 && angle_converted > -10 ) {
						myLEDsButtons.setLED((byte)1);
					}
					else {
						myLEDsButtons.resetLED((byte)1);
					}
				}
				
				/* Add offset and factor (both depending on graphics) */
				angle_converted = (int)((angle_converted+35)*5);
				
				myAcceleratorChangeListener.onAcceleratorChange(angle_converted);
			}
		}
	}
	
	
	/**
	 * constructor of MainModel
	 */
	private MainThread() {
		
		myLEDsButtons = new LEDsButtons();
		mySensor = new MotionSensor();
		
		myThread = new Thread(this, "HardwareThread");

	};
	
	
	/**
	 * for creating a singleton of MainModel
	 * @param handler handler to UI for refreshing
	 * 
	 * @return reference to model if success, instead null 
	 */
	public static MainThread create(SurfaceHolder holder, BubbleView view) {
		
		if(myHolder == null) {
			myHolder = holder;
			myView = view;
			return new MainThread();
		} else {
			return null;
		}
	}
	
	
	/**
	 * start the thread
	 */
	public void start() {
		myThread.start();
	}
	
	
	/**
	 * stops thread and gives the singleton free
	 */
	public void done() {

//		/* Turn off LEDs and unexport all (incl. buttons) */
//		myLEDsButtons.unexport();
//		myLEDsButtons = null;
//		
//		boolean retry = true;
//		while (retry) {
//			try {
//				myThread.join();
//				retry = false;
//			} catch (InterruptedException e) {
//				// try again shutting down the thread
//			}
//		}
//		myHolder = null;
		myDone = true;

//		myHandlerThread.quit();
	}
		

	/**
	 * read current button state 
	 */
	private Runnable buttonRefresh = new Runnable() {
		
		@Override
		public void run() {
			
			/* Read button with edge detection */
			boolean current_btn_value = myLEDsButtons.getButton((byte) 1);
			if(current_btn_value && !last_btn_value) {
			
				mySensor.setAsDefaultRot();
			}
			last_btn_value = current_btn_value;
			
			myButtonHandler.postDelayed(this, BUTTON_REFRESH_TIME);
		}
	};
	
	
	/**
	 * read current accelerator state
	 */
	private Runnable acceleratorRefresh = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			myRotation = mySensor.getRotRPY();
			
			notifyListener();
			
			myAcceleratorHandler.postDelayed(this, ACCELERATOR_REFRESH_TIME);
		}
	};
	
	
	

	
	/**
	 * UI refreshing and model-logic
	 */
	@Override
	public void run() {
		
		Canvas canvas;
		
		long beginTime;		// the time when the cycle begun
		long timeDiff;		// the time it took for the cycle to execute
		int sleepTime = 0;		// ms to sleep (<0 if we're behind)
		int framesSkipped;	// number of frames being skipped 
		
		myHandlerThread = new HandlerThread("MyHandlerThread");
		myHandlerThread.start();
		
		/* starts hardware read-in's */
		myButtonHandler = new Handler(myHandlerThread.getLooper());
		myButtonHandler.postDelayed(buttonRefresh, BUTTON_REFRESH_TIME);
		
		myAcceleratorHandler = new Handler(myHandlerThread.getLooper());
		myAcceleratorHandler.postDelayed(acceleratorRefresh, ACCELERATOR_REFRESH_TIME);
		
		Log.d(TAG, "Starting game loop");
		
		/* endless loop until done */
		while(myDone == false) {
			canvas = null;
			
			try {
				canvas = myHolder.lockCanvas();
				
				synchronized (myHolder) {
					beginTime = System.currentTimeMillis();
					
					framesSkipped = 0;	/* resetting the frames skipped */
					/* update game state */
					myView.update();
					/* render state to the screen
					   draws the canvas on the panel */
					if(canvas != null)
						myView.render(canvas);				
					/* calculate how long did the cycle take */
					timeDiff = System.currentTimeMillis() - beginTime;
					/* calculate sleep time */
					sleepTime = (int)(FRAME_PERIOD - timeDiff);
					
					if (sleepTime > 0) {
						/* if sleepTime > 0 we're OK */
						try {
							/* send the thread to sleep for a short period
							   very useful for battery saving */
							Thread.sleep(sleepTime);	
						} catch (InterruptedException e) {}
					}
					
					while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
						/* we need to catch up
						   update without rendering */
						myView.update(); 
						/* add frame period to check if in next frame */
						sleepTime += FRAME_PERIOD;	
						framesSkipped++;
					}
				}
			} finally {
				/* in case of an exception the surface is not left in 
				   an inconsistent state */
				if (canvas != null) {
					myHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
		
		myHolder = null;
		myHandlerThread.quit();
		
		/* Turn off LEDs and unexport all (incl. buttons) */
		myLEDsButtons.unexport();
		myLEDsButtons = null;
		
		boolean retry = true;
		while (retry) {
			try {
				myThread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		
		
		
	}
}