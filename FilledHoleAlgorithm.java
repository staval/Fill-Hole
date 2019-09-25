import org.opencv.core.Point;

import java.util.Set;

public abstract class FilledHoleAlgorithm {
	
	protected WeightFunction w;

	public FilledHoleAlgorithm(WeightFunction w) {
		this.w = w;
	}

    public abstract void filledHole(Image img);

	public void setW(WeightFunction newWeightFunc) {
		this.w = newWeightFunc;
	}

	protected double calculatePixelI(Image img, Set<Point> filledBasedOn, Point pixel){

        double numerator = 0;
        double fraction = 0;

        for(var boundaryP: filledBasedOn) {
            double weight = this.w.getWeight(pixel, boundaryP);
            numerator += weight*img.getPixelI(boundaryP);
            fraction += weight;
        }
        return (fraction != 0) ? numerator/fraction : -1;
    }

    protected double calculatePixelI(Image img, Point pixel){
	    return calculatePixelI(img, img.getBoundaryPixels(), pixel);
    }



    }

