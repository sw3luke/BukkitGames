package utilities;

public class Border {
	public int radiusSq;
	public int definiteSq;
	public double centerX;
	public double centerZ;
	public double radius;

	public Border(double X, double Z, double Radius) {
		this.centerX = X;
		this.centerZ = Z;
		this.radius = Radius;
		this.radiusSq = (int) (this.radius * this.radius);
		this.definiteSq = (int) Math.sqrt(0.5D * this.radiusSq);
	}

	public String toString() {
		return "X: " + this.centerX + " Z: " + this.centerZ + " Radius: "
				+ this.radius;
	}
}