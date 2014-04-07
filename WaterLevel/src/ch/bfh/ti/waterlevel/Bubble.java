package ch.bfh.ti.waterlevel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * model
 * @author grosi
 *
 */

public final class Bubble {
	
	private final float KI = 5;
	private final float TA = 0.02F;
	
	private Bitmap myBubble1;
	private Bitmap myBubble2;
	private Bitmap myBubble3;
	
	private int myX;
	private int myLastX;
	private	int myEsum;
	private Bitmap myCurrentBubble;
	
	
	public Bubble(Context context) {
		
		Resources res = context.getResources();
		
		/* load resources */
		myBubble1 = BitmapFactory.decodeResource(res, R.drawable.bubble1);
		myBubble2 = BitmapFactory.decodeResource(res, R.drawable.bubble2);
		myBubble3 = BitmapFactory.decodeResource(res, R.drawable.bubble3);
	}
	
	
	/**
	 * set next bubble position 
	 * @param x
	 */
	public void setPosition(int x) {
		myX = x;
	}
	

	/**
	 * draw bubble
	 * @param canvas reference to a canvas for drawing the bubble
	 */
	public void draw(Canvas canvas) {
		canvas.drawBitmap(myCurrentBubble, myLastX, 0, null);
	}
	
	
	/**
	 * update model
	 */
	public void update() {
			
			myEsum += myX - myLastX;
			
 			myLastX = (int) (KI * myEsum * TA);
			
 			if(myLastX < myX) {
 				myCurrentBubble = myBubble3;
 			} else if(myLastX == myX) {
 				myCurrentBubble = myBubble2;
 			} else {
 				myCurrentBubble = myBubble1;
 			}
	}

}