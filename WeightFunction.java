import org.opencv.core.Point;


public interface WeightFunction {

	public double getWeight(Point p1, Point p2);

}


class DifoltWeightFunction implements WeightFunction{
	
	private final double epsilon;
	private final double z;
	
	public DifoltWeightFunction(double z, double epsilon) {
		this.epsilon = epsilon;
		this.z = z;
	}
	
	
	public static double EuclideanDistance(Point p1, Point p2){
		return Math.pow(Math.pow((p1.x - p2.x), 2) + Math.pow((p1.y - p2.y), 2), 0.5);
	}

	
	public double getWeight(Point p1, Point p2) {
		
		double fraction = Math.pow(DifoltWeightFunction.EuclideanDistance(p1, p2), this.z) + this.epsilon;
		return (fraction != 0) ? 1/fraction : -1;	
	}
	

}
