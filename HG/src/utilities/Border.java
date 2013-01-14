package utilities;

import utilities.enums.BorderType;

public class Border {
	public int radiusSq;
	public int definiteSq;
	public double centerX;
	public double centerZ;
	public double radius;
	public BorderType type;

	public Border(double X, double Z, double Radius, BorderType t) {
		centerX = X;
		centerZ = Z;
		radius = Radius;
		radiusSq = (int) (radius * radius);
		definiteSq = (int) Math.sqrt(0.5D * radiusSq);
		type = t;
	}

	public String toString() {
		return "X: " + centerX + " Z: " + centerZ + " Radius: "
				+ radius + " Type: " + type;
	}
}