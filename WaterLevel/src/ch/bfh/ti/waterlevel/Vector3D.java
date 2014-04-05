package ch.bfh.ti.waterlevel;

/* Very simple 3D vector class */
public class Vector3D {
	
	public int x;
	public int y;
	public int z;
	
	/* Constructor */
	Vector3D() {
		
		x = 0;
		y = 0;
		z = 0;
	}
	/* Constructor with parameters */
	Vector3D(int _x, int _y, int _z) {
		
		x = _x;
		y = _y;
		z = _z;
	}
}
