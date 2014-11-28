package com.mjroundi.FreeHandPolygonLib;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Point;



/**
 * Reduces the number of points in a shape using the Douglas-Peucker algorithm. <br>
 * From:
 * http://www.phpriot.com/articles/reducing-map-path-douglas-peucker-algorithm/4<br>
 * Ported from PHP to Java. "marked" array added to optimize.
 * Adapted by M.Jroundi to use pixels instead of longitudes and latitudes
 * @author M.Kergall
 */
public class DouglasPeuckerReducer {

	/**
	 * Reduce the number of points in a shape using the Douglas-Peucker
	 * algorithm
	 * @param shape
	 *            The shape to reduce
	 * @param tolerance
	 *            The tolerance to decide whether or not to keep a point, in the
	 *            coordinate system of the points (micro-degrees here)
	 * @return the reduced shape
	 */
	public static List<Point> reduceWithTolerance(List<Point> shape,
			double tolerance)
	{
		int n = shape.size();
		// if a shape has 2 or less points it cannot be reduced
		if (tolerance <= 0 || n < 3) {
			return shape;
		}

		boolean[] marked = new boolean[n]; //vertex indexes to keep will be marked as "true"
		for (int i = 1; i < n - 1; i++)
			marked[i] = false;
		// automatically add the first and last point to the returned shape
		marked[0] = marked[n - 1] = true;

		// the first and last points in the original shape are
		// used as the entry point to the algorithm.
		douglasPeuckerReduction(
				shape, // original shape
				marked, // reduced shape
				tolerance, // tolerance
				0, // index of first point
				n - 1 // index of last point
		);

		// all done, return the reduced shape
		ArrayList<Point> newShape = new ArrayList<Point>(n); // the new shape to return
		for (int i = 0; i < n; i++) {
			if (marked[i])
				newShape.add(shape.get(i));
		}
		return newShape;
	}

	/**
	 * Reduce the points in shape between the specified first and last index.
	 * Mark the points to keep in marked[]
	 * @param shape
	 *            The original shape
	 * @param marked
	 *            The points to keep (marked as true)
	 * @param tolerance
	 *            The tolerance to determine if a point is kept
	 * @param firstIdx
	 *            The index in original shape's point of the starting point for
	 *            this line segment
	 * @param lastIdx
	 *            The index in original shape's point of the ending point for
	 *            this line segment
	 */
	private static void douglasPeuckerReduction(List<Point> shape, boolean[] marked,
			double tolerance, int firstIdx, int lastIdx)
	{
		if (lastIdx <= firstIdx + 1) {
			// overlapping indexes, just return
			return;
		}

		// loop over the points between the first and last points
		// and find the point that is the farthest away

		double maxDistance = 0.0;
		int indexFarthest = 0;

		Point firstPoint = shape.get(firstIdx);
		Point lastPoint = shape.get(lastIdx);

		for (int idx = firstIdx + 1; idx < lastIdx; idx++) {
			Point point = shape.get(idx);

			double distance = orthogonalDistance(point, firstPoint, lastPoint);

			// keep the point with the greatest distance
			if (distance > maxDistance) {
				maxDistance = distance;
				indexFarthest = idx;
			}
		}

		if (maxDistance > tolerance) {
			//The farthest point is outside the tolerance: it is marked and the algorithm continues. 
			marked[indexFarthest] = true;

			// reduce the shape between the starting point to newly found point
			douglasPeuckerReduction(shape, marked, tolerance, firstIdx, indexFarthest);

			// reduce the shape between the newly found point and the finishing point
			douglasPeuckerReduction(shape, marked, tolerance, indexFarthest, lastIdx);
		}
		//else: the farthest point is within the tolerance, the whole segment is discarded.
	}

	/**
	 * Calculate the orthogonal distance from the line joining the lineStart and
	 * lineEnd points to point
	 * @param point
	 *            The point the distance is being calculated for
	 * @param lineStart
	 *            The point that starts the line
	 * @param lineEnd
	 *            The point that ends the line
	 * @return The distance in points coordinate system
	 */
	public static double orthogonalDistance(Point point, Point lineStart, Point lineEnd)
	{
		double area = Math.abs(
				(
				1.0 * lineStart.x * lineEnd.y
						+ 1.0 * lineEnd.x * point.y
						+ 1.0 * point.x * lineStart.y
						- 1.0 * lineEnd.x * lineStart.y
						- 1.0 * point.x * lineEnd.y
						- 1.0 * lineStart.x * point.y
				) / 2.0
				);

		double bottom = Math.hypot(
				lineStart.x - lineEnd.x,
				lineStart.y - lineEnd.y
				);

		return (area / bottom * 2.0);
	}
}