package ch.bfh.ti.waterlevel;

/* Very simple 3D vector class */
public class Vector3D {
	
	public double x;
	public double y;
	public double z;
	
	/* Constructor */
	public Vector3D() {
		
		x = 0;
		y = 0;
		z = 0;
	}
	/* Constructor with parameters */
	public Vector3D(double _x, double _y, double _z) {
		
		x = _x;
		y = _y;
		z = _z;
	}
	
	/* Calculate the absolute value */
	public double getNorm() {
		return Math.sqrt(x*x + y*y + z*z);
	}
}
