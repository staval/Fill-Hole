import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class Image {

    private Mat matrix;

    private Set<Point> hole;
    private Set<Point> boundaryPixels;

    private boolean isInitialize;
    private final double holeInd = -1.0;

    public Function<Point, Set<Point>> getNeighbors;

    public Image(Mat matrix, String connectivity) {

        this.matrix = matrix;

        this.hole = new HashSet<Point>();
        this.boundaryPixels = new HashSet<Point>();

        this.isInitialize = false;

        if(connectivity.equals("8"))
            this.getNeighbors = this::findEightNeighbors;
        else
            this.getNeighbors = this::findFourNeighbors;
    }

    public Image(String path, String connectivity){
        this(Image.scaleImageMatrix(Image.loadImgMatrix(path), 1.f/255), connectivity);
    }

    public boolean isPointInHole(Point p) {
        return (this.matrix.get((int)p.x, (int)p.y)[0] == this.holeInd);
    }

    private void updateBoundary(Point p) {

        if( ! this.isPointInHole(p))
            return;

        Set<Point> pNeighbors = this.getNeighbors.apply(p);

        for(var n: pNeighbors)
            if( ! isPointInHole(n))
                this.boundaryPixels.add(n);
    }

    private void initHole() {

        for (var p : new MatIter(this.matrix))
            if (this.isPointInHole(p)) {
                this.hole.add(p);
                this.updateBoundary(p);
            }

        this.isInitialize = true;
    }

    public Set<Point> getHolePixels() {
        if( ! this.isInitialize)
            this.initHole();

        return this.hole;
    }

    public Set<Point> getBoundaryPixels() {
        if( ! this.isInitialize)
            this.initHole();

        return this.boundaryPixels;
    }

    public double getPixelI(Point pixel) {
        return this.matrix.get((int)pixel.x, (int)pixel.y)[0];
    }

    public void printHole(){

        for(Point p: this.getHolePixels())
            System.out.println("I("+p+") = "+this.getPixelI(p));
    }

    public double updatePixelVal(Point pixel, double newVal){

        double oldVal = this.matrix.get((int)pixel.x, (int)pixel.y)[0];
        this.matrix.put((int)pixel.x, (int)pixel.y, newVal);

        return oldVal;
    }

    // clear hole and boundaryPixels fields
    public void zerosHole(){
        this.hole.clear();
        this.boundaryPixels.clear();

    }

    public Mat getMatrix(){
        return this.matrix;
    }

    public boolean isImgComplete(){

        if( ! this.hole.isEmpty()) {
            System.out.println("\nhole field ! empty");
            return false;
        }

        if( ! this.boundaryPixels.isEmpty()){
            System.out.println("\nboundaryPixels field ! empty");
            return false;
        }

        for(var p: new MatIter(this.matrix))
            if(this.getPixelI(p) == -1){
                System.out.println("\nfields empty but there is holed");
                return false;
            }

        return true;
    }

    public static Mat scaleImageMatrix(Mat srcMat, double alpha){

//        1.f/255

        // scale image
        Mat scaleMatrix = new Mat();
        srcMat.convertTo(scaleMatrix, CvType.CV_64FC1, alpha);

        return scaleMatrix;
    }

    public static Mat loadImgMatrix(String path) {

        // open image
        Mat destImg = Imgcodecs.imread(path,  CvType.CV_8UC1);

        if (destImg.empty())
            throw new IllegalArgumentException("my error: (laudImg) can nat open recived img");

        return destImg;
    }

    public static Mat resizeImg(Mat srcImg, int cols, int rows){

        Mat resizeImg = new Mat();
        Imgproc.resize(srcImg, resizeImg, new Size(cols, rows));

        return resizeImg;
    }

    public static void printMatrix(Mat matrix) {
        System.out.println(matrix.dump());
    }

    private Set<Point> findFourNeighbors(Point point){
        Set<Point> outputNeighbors = new HashSet<>();

        int[] toAdd = {-1, 0, 1};

        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Point neighbor = new Point(point.x + toAdd[i], point.y +toAdd[j]);
                if( ! point.equals(neighbor) && Image.pointInMatrix(this.matrix, point))
                    outputNeighbors.add(neighbor);
            }

        }
        return outputNeighbors;
    }

    private Set<Point> findEightNeighbors(Point point){
        Set<Point> outputNeighbors = new HashSet<>();
        for (int xi = (int) point.x - 1; xi <= point.x + 1; xi++) {
            for (int yi = (int) point.y - 1; yi <= point.y + 1; yi++) {

                Point neighbor = new Point(xi, yi);

                if (neighbor.equals(point))
                    continue;

                if (Image.pointInMatrix(this.matrix, neighbor))
                    outputNeighbors.add(neighbor);
            }
        }
        return outputNeighbors;
    }

    public static boolean pointInMatrix(Mat matrix, Point point) {
        if (point.x < 0 || point.y < 0)
            return false;

        if (point.x >= matrix.cols() || point.y >= matrix.rows())
            return false;

        return true;
    }

    public double getHoleInd(){
        return this.holeInd;
    }
}
