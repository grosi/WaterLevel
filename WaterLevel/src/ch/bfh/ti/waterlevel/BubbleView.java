package ch.bfh.ti.waterlevel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Controller and View
 * @author grosi
 *
 */

public class BubbleView extends SurfaceView implements SurfaceHolder.Callback {

	private MainThread myThread;
	
	private Bubble myBubble;

	public BubbleView(Context context, AttributeSet attrs) {
		super(context,attrs);

		myBubble = new Bubble(context);
		
		getHolder().addCallback(this);
		myThread = MainThread.create(getHolder(), this);	
		
		/* set thread listener */
		myThread.setAcceleratorChangeListener(new MainThread.AcceleratorChangeListener() {
			
			@Override
			public void onAcceleratorChange(int angle) {
				
				myBubble.setPosition(angle);
			}
		});
	}
	

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		myThread.start();
	}
	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		myThread.done();
	}
	
	
	/**
	 * redraw the surface view
	 * @param canvas reference to canvas of surfaceview
	 */
	public void render(Canvas canvas) {
		canvas.drawColor(Color.WHITE);
		myBubble.draw(canvas);
	}
	
	
	/**
	 * This is the game update method. It iterates through all the objects
	 * and calls their update method if they have one or calls specific
	 * engine's update method.
	 */
	public void update() {

		myBubble.update();
	}

}