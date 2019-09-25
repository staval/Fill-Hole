import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class App {

    public void run(String[] args) {

        nu.pattern.OpenCV.loadShared();

        // create an Image Obj for the input image with the input hole
        Image srcImg = ArgsCreator.createSrcImg(args);

        // create an input algorithm Obj
        FilledHoleAlgorithm algo = ArgsCreator.createAlgorithm(args);

        algo.filledHole(srcImg);

        Mat output = Image.scaleImageMatrix(srcImg.getMatrix(), 255);

        Imgcodecs.imwrite(ArgsCreator.getSrcImgPath(args), output);

    }
}
