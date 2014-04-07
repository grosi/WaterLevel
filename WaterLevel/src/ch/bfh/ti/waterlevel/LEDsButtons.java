package ch.bfh.ti.waterlevel;

public class LEDsButtons {

	/* BFH cape LED and button defines */
	private final String LED_L1    =  "61";
	private final String LED_L2    =  "44";
	private final String LED_L3    =  "68";
	private final String LED_L4    =  "67";
	private final String BUTTON_T1 =  "49";
	private final String BUTTON_T2 = "112";
	private final String BUTTON_T3 =  "51";
	private final String BUTTON_T4 =   "7";
	
	/* Define some useful constants */
	private final char   ON        =   '0';
	private final char   OFF       =   '1';
	private final String PRESSED   =   "0";
	
	/* Create new gpio object */
	private final SysfsFileGPIO gpio = new SysfsFileGPIO();
	
	/* Constructor */
	public LEDsButtons() {
		
		/* Export GPIOs */
		gpio.export(LED_L1);
		gpio.export(LED_L2);
		gpio.export(LED_L3);
		gpio.export(LED_L4);
		gpio.export(BUTTON_T1);
		gpio.export(BUTTON_T2);
		gpio.export(BUTTON_T3);
		gpio.export(BUTTON_T4);
		/* Set direction */
		gpio.set_direction_out(LED_L1);
		gpio.set_direction_out(LED_L2);
		gpio.set_direction_out(LED_L3);
		gpio.set_direction_out(LED_L4);
		gpio.set_direction_out(BUTTON_T1);
		gpio.set_direction_out(BUTTON_T2);
		gpio.set_direction_out(BUTTON_T3);
		gpio.set_direction_out(BUTTON_T4);
	}

	/* Set LEDs */
	public void setLED(byte led_nr) {
		
		switch(led_nr) {
		case 1:
			gpio.write_value(LED_L1, ON);
			break;
			
		case 2:
			gpio.write_value(LED_L2, ON);
			break;
			
		case 3:
			gpio.write_value(LED_L3, ON);
			break;
			
		case 4:
			gpio.write_value(LED_L4, ON);
			break;
			
		default:
			/* Do nothing for other values */
			break;
		}
	}
	
	/* Set all LEDs */
	public void setLEDsAll() {
		
		gpio.write_value(LED_L1, ON);
		gpio.write_value(LED_L2, ON);
		gpio.write_value(LED_L3, ON);
		gpio.write_value(LED_L4, ON);
	}
	
	/* Reset LEDs */
	public void resetLED(byte led_nr) {
		
		switch(led_nr) {
		case 1:
			gpio.write_value(LED_L1, OFF);
			break;
			
		case 2:
			gpio.write_value(LED_L2, OFF);
			break;
			
		case 3:
			gpio.write_value(LED_L3, OFF);
			break;
			
		case 4:
			gpio.write_value(LED_L4, OFF);
			break;
			
		default:
			/* Do nothing for other values */
			break;
		}
	}
	
	/* Reset all LEDs */
	public void resetLEDsAll() {
		
		gpio.write_value(LED_L1, OFF);
		gpio.write_value(LED_L2, OFF);
		gpio.write_value(LED_L3, OFF);
		gpio.write_value(LED_L4, OFF);
	}
	
	/* Read the button, true = pressed, false = not pressed */
	public boolean getButton(byte btn_nr) {
		
		String value = "1";
		boolean retval = false;
		
		switch(btn_nr) {
		case 1:
			value = gpio.read_value(BUTTON_T1);
			break;
			
		case 2:
			value = gpio.read_value(BUTTON_T2);
			break;
			
		case 3:
			value = gpio.read_value(BUTTON_T3);
			break;
			
		case 4:
			value = gpio.read_value(BUTTON_T4);
			break;
			
		default:
			/* Do nothing for other values */
			break;
		}
		
		/* Convert return value */
		if(value.equals(PRESSED)) {
			retval = true;
		}
		else {
			retval = false;
		}
		
		return retval;
	}

	/* Reset all LEDs and then unexport all LEDs and buttons */
	public void unexport() {
		
		/* Reset LEDs */
		resetLEDsAll();
		/* Unexport GPIOs */
		gpio.unexport(LED_L1);
		gpio.unexport(LED_L2);
		gpio.unexport(LED_L3);
		gpio.unexport(LED_L4);
		gpio.unexport(BUTTON_T1);
		gpio.unexport(BUTTON_T2);
		gpio.unexport(BUTTON_T3);
		gpio.unexport(BUTTON_T4);
	}
}
