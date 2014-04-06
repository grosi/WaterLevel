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
	
	/* Add an offset and return value */
	public Vector3D addOffset(Vector3D offset) {
		
		/* Add values together */
		offset.x += x;
		offset.y += y;
		offset.z += z;
		
		return offset;
	}
	
	/* Subtract an offset and return value */
	public Vector3D subOffset(Vector3D offset) {
		
		/* Subtract offset from value */
		offset.x = x - offset.x;
		offset.y = y - offset.y;
		offset.z = z - offset.z;
		
		return offset;
	}
}
