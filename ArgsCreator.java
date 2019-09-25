import org.opencv.core.Mat;

public class ArgsCreator {

    public static String getSrcImgPath(String[] args){
        return args[0];
    }

    public static Mat createImageWithHole(String srcImgPath, String holeShapeImgPath){

        Mat srcImg = Image.loadImgMatrix(srcImgPath);

        Mat holeShapeImg = Image.loadImgMatrix(holeShapeImgPath);

        // scale pixels value to be in [0, 1]
        srcImg = Image.scaleImageMatrix(srcImg, 1.0/255);
        holeShapeImg = Image.scaleImageMatrix(holeShapeImg, 1.0/255);

        // resize hole image to be with srcImg shape
        Mat resizeHoleImg = Image.resizeImg(holeShapeImg, srcImg.cols(), srcImg.rows());

        // create the hole
        for(var p: new MatIter(srcImg))
            if(resizeHoleImg.get((int)p.x, (int)p.y)[0] != 1)
                srcImg.put((int)p.x, (int)p.y, -1.0);

        return srcImg;
    }

    public static Image createSrcImg(String[] args){

        String imgPath = args[0];
        String holeShapePath = args[1];

        Mat imgMatrix = createImageWithHole(imgPath, holeShapePath);

        String connectivity = args[4];
        return new Image(imgMatrix, connectivity);
    }

    public static DifoltWeightFunction createWeightFunction(String[] args){
        float z = Float.parseFloat(args[2]);
        float epsilon =  Float.parseFloat(args[3]);

        return new DifoltWeightFunction(z, epsilon);
    }

    public static FilledHoleAlgorithm createAlgorithm(String[] args){

        if(args.length >= 6 && ! args[5].equals("NEARBY"))
            throw new IllegalArgumentException("Unknown Algorithm");

        WeightFunction w = ArgsCreator.createWeightFunction(args);
        return new NaiveAlgorithm(w);
    }
}
