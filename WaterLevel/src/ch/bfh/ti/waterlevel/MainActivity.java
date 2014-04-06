package ch.bfh.ti.waterlevel;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private MotionSensor sens;
	private LEDsButtons ledbtn;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
        

        /* TODO: Just for debugging */
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        sens = new MotionSensor();
        Vector3D acc = sens.getAcceleration();
        textView1.setText("Acc: (" + acc.x + ", " + acc.y + ", " + acc.z + ")");
        Vector3D rot = sens.getRotEuler();
        textView1.append("\nEuler: (" + rot.x + ", " + rot.y + ", " + rot.z + ")");
        rot = sens.getRotRPY();
        textView1.append("\nR-P-Y: (" + rot.x + ", " + rot.y + ", " + rot.z + ")");
        ledbtn = new LEDsButtons();
        ledbtn.setLEDsAll();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

	@Override
	protected void onStop() {

		/* Close the I2C connection */
		sens.close();
		/* Reset LEDs and unexport LEDs and buttons */
		ledbtn.unexport();

		finish();
		super.onStop();
	}
}
