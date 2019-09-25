import org.opencv.core.Point;

public class NaiveAlgorithm extends FilledHoleAlgorithm {

	public NaiveAlgorithm(WeightFunction w) {
		super(w);
	}

    @Override
    public void filledHole(Image img){

        for(var p: img.getHolePixels()){
            double pI =  this.calculatePixelI(img, p);
            img.updatePixelVal(p, pI);
        }

    }

}
