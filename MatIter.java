import java.util.Iterator;
import java.util.NoSuchElementException;

import org.opencv.core.Mat;
import org.opencv.core.Point;

public class MatIter implements Iterable<Point> {

	private Mat matrix;

	public MatIter(Mat matrix) {
		this.matrix = matrix;
	}

	@Override
	public Iterator<Point> iterator() {
		return new Iterator<>() {

			private Point current = new Point(0, 0);

			@Override
			public boolean hasNext() {
				return (current.x < matrix.cols() && current.y < matrix.rows());
			}

			@Override
			public Point next() {
				if (hasNext()) {
					
					var tmp = current.clone();
					
					if (current.x < matrix.cols()-1) {
						current.x++;
					} else {
						current.x = 0;
						current.y++;
					}
					return tmp;

				} else {
					throw new NoSuchElementException("Range reached the end");
				}
			}

		};
	}
}
