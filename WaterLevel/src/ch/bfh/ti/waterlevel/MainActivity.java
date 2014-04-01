package ch.bfh.ti.waterlevel;

import ch.bfh.ti.waterlevel.I2C;
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

	//TODO start
	/* MCP9800 Register pointers */
	private static final char MCP9800_TEMP   = 0x00;      /* Ambient Temperature Register */
	private static final char MCP9800_CONFIG = 0x01;      /* Sensor Configuration Register */

	/* Sensor Configuration Register Bits */
	private static final char MCP9800_12_BIT = 0x60;

	/* i2c Address of MCP9802 device */
	private static final char MCP9800_I2C_ADDR = 0x48;

	/* i2c device file name */
	private static final String MCP9800_FILE_NAME = "/dev/i2c-3";

	I2C i2c;
	int[] i2cCommBuffer = new int[16];
	int fileHande;

	double TempC;
	int Temperature;

	/* Define widgets */
	TextView textView1;

	/* Temperature Degrees Celsius text symbol */
	private static final String DEGREE_SYMBOL = "\u2103";
	//TODO end
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            getFragmentManager().beginTransaction()
//                    .add(R.id.container, new PlaceholderFragment())
//                    .commit();
//        }
        
        //TODO start
        textView1 = (TextView) findViewById(R.id.textView1);
  	  
    	/* Instantiate the new i2c device */
    	i2c = new I2C();
    	    
    	/* Open the i2c device */
    	fileHande = i2c.open(MCP9800_FILE_NAME);
    	    
    	/* Set the I2C slave address for all subsequent I2C device transfers */
    	i2c.SetSlaveAddres(fileHande, MCP9800_I2C_ADDR);
    	    
    	/* Setup i2c buffer for the configuration register */
    	i2cCommBuffer[0] = MCP9800_CONFIG;
    	i2cCommBuffer[1] = MCP9800_12_BIT;
    	i2c.write(fileHande, i2cCommBuffer, 2);

    	/* Setup mcp9800 register to read the temperature */
    	i2cCommBuffer[0] = MCP9800_TEMP;
    	i2c.write(fileHande, i2cCommBuffer, 1);
    	    
    	/* Read the current temperature from the mcp9800 device */ 
    	i2c.read(fileHande, i2cCommBuffer, 2);

    	/* Assemble the temperature values */
    	Temperature = ((i2cCommBuffer[0] << 8) | i2cCommBuffer[1]);
    	Temperature = Temperature >> 4;

    	/* Convert current temperature to float */
    	TempC = 1.0 * Temperature * 0.0625;
    	    
        /* Display actual temperature */
        textView1.setText("Temperature: " + String.format("%3.2f", TempC) + DEGREE_SYMBOL); 

    	/* Close the i2c file */
    	i2c.close(fileHande);
    	//TODO end
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

}
