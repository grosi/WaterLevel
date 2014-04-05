package ch.bfh.ti.waterlevel;

public class MotionSensor {

	/* LIS302DL infos */
	private static final byte   LIS302DL_CONFIG    = 0x20;          /* LIS302DL register pointer */
	private static final byte   LIS302DL_12_BIT    = 0x67;          /* Sensor config register bits */
	private static final byte   LIS302DL_I2C_ADDR  = 0x1C;          /* i2c slave address */
	private static final String LIS302DL_FILE_NAME = "/dev/i2c-3";  /* i2c device file */
	
	/* LIS302DL registers */
	private static final byte   LIS302DL_OUT_X     = 0x29;          /* Register for the x acceleration */
	private static final byte   LIS302DL_OUT_Y     = 0x2B;          /* Register for the y acceleration */
	private static final byte   LIS302DL_OUT_Z     = 0x2D;          /* Register for the z acceleration */
	
	/* I2C variables */
	private I2C i2c;
	private int[] i2cBuffer = new int[2];
	private int fileHandle;
	
	/* Acceleration variables */
	private Vector3D acceleration;
	private double norm;
	/* Rotation variable, used for both, euler or roll-pitch-yaw */
	private Vector3D rotation;
	
	/* Constructor */
	public MotionSensor() {
		
		/* Create the attribute objects */
		acceleration = new Vector3D();
		rotation = new Vector3D();
		
		/* Create the i2c device */
		i2c = new I2C();
		
		if (i2c != null) {
			
			/* Open the i2c device */
			fileHandle = i2c.open(LIS302DL_FILE_NAME);
			
			/* Check if successful */
			if (fileHandle != 0) {
				
				/* Set the slave address */
				i2c.SetSlaveAddress(fileHandle, LIS302DL_I2C_ADDR);
				
				/* Setup i2c buffer for the configuration register */
				i2cBuffer[0] = LIS302DL_CONFIG;
				i2cBuffer[1] = LIS302DL_12_BIT;
				i2c.write(fileHandle, i2cBuffer, 2);
			}
		}
	}
	
	/* Get the acceleration values by I2C */
	public Vector3D getAcceleration() {
		
		if (i2c != null) {
			
			/* Transmit the register and then read the x acceleration */
			i2cBuffer[0] = LIS302DL_OUT_X;
			i2c.write(fileHandle, i2cBuffer, 1);
			i2c.read(fileHandle, i2cBuffer, 1);
			/* Convert the 2’s complement number */
			if(i2cBuffer[0] > 0x7F){
				i2cBuffer[0] -= 0xFF;
			}
			/* Store the value in the variable */
			acceleration.x = i2cBuffer[0];
			
			/* Transmit the register and then read the y acceleration */
			i2cBuffer[0] = LIS302DL_OUT_Y;
			i2c.write(fileHandle, i2cBuffer, 1);
			i2c.read(fileHandle, i2cBuffer, 1);
			/* Convert the 2’s complement number */
			if(i2cBuffer[0] > 0x7F){
				i2cBuffer[0] -= 0xFF;
			}
			/* Store the value in the variable */
			acceleration.y = i2cBuffer[0];
			
			/* Transmit the register and then read the z acceleration */
			i2cBuffer[0] = LIS302DL_OUT_Z;
			i2c.write(fileHandle, i2cBuffer, 1);
			i2c.read(fileHandle, i2cBuffer, 1);
			/* Convert the 2’s complement number */
			if(i2cBuffer[0] > 0x7F){
				i2cBuffer[0] -= 0xFF;
			}
			/* Store the value in the variable */
			acceleration.z = i2cBuffer[0];
		}
		
		return acceleration;
	}
	
	/* Get the acceleration values by I2C and calculate the euler rotation values in rad */
	public Vector3D getRotEuler() {
		
		/* Update the acceleration data first */
		getAcceleration();
		/* Calculate the absolute value */
		norm = acceleration.getNorm();
		
		/* Calculate alpha */
		rotation.x = Math.asin(acceleration.x / norm);
		/* Calculate beta */
		rotation.y = Math.asin(acceleration.y / norm);
		/* Calculate gamma */
		rotation.z = Math.acos(acceleration.z / norm);
		
		return rotation;
	}
	
	/* Get the acceleration values by I2C and calculate the roll-pitch-yaw rotation values in rad */
	public Vector3D getRotRPY() {
		
		/* Update the acceleration data first */
		getAcceleration();
		
		/* Calculate roll */
		rotation.x = Math.atan(acceleration.x / Math.sqrt(acceleration.y * acceleration.y + acceleration.z * acceleration.z));
		/* Calculate pitch */
		rotation.y = Math.atan(acceleration.y / Math.sqrt(acceleration.x * acceleration.x + acceleration.z * acceleration.z));
		/* Set yaw to 0 */
		rotation.z = 0;
		
		return rotation;
	}
	
	/* Close the I2C connection */
	public void close() {
		
		if (i2c != null) {
			
			/* Close the i2c file */
			i2c.close(fileHandle);
		}
	}
}
