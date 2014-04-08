package ch.bfh.ti.waterlevel;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	
	private BubbleView myView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main_test);
        
        myView = (BubbleView)findViewById(R.id.bubble_view_2);
    }
    
    
    @Override
	protected void onStop() {
		super.onStop();		
		//finish();
	}
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}