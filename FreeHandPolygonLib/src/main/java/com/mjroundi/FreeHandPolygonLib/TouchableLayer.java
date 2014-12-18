package com.mjroundi.FreeHandPolygonLib;

import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * FrameLayout where the preview of the polygon is displayed
 * before the user stops touching screen
 */
public class TouchableLayer extends FrameLayout {
    /**
     * the path of the preview representing the
     */
    private Path mPath = new Path();
    /**
     * Free hand drawer containing configuration for drawing
     */
    private FreeHandDrawer mFreeHandDrawer;
    /**
     * List of point filled in TouchableLayer.onTouchEvent
     * (MotionEvent.ACTION_MOVE) when the finger is moving
     */
    private List<Point> mPoints = new ArrayList<Point>();
    /**
     * Listener on polygon draw
     */
    private OnPolygonDrawListener onPolygonDrawListener;

    public TouchableLayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setBackgroundColor(Color.TRANSPARENT);
        setLocked(false);
    }
    public TouchableLayer(Context context, AttributeSet attrs){
        super(context, attrs);
        this.setBackgroundColor(Color.TRANSPARENT);
        setLocked(false);
    }

    public TouchableLayer(Context context) {
        super(context);
        this.setBackgroundColor(Color.TRANSPARENT);
        setLocked(false);
    }

    public void setFreeHandDrawer(FreeHandDrawer freeHandDrawer) {
        this.mFreeHandDrawer = freeHandDrawer;
    }

    public void setOnPolygonDrawListener(OnPolygonDrawListener onPolygonDrawListener) {
        this.onPolygonDrawListener = onPolygonDrawListener;
    }
    /**
     * @param points List<Point>
     * @return true if points are defining a polygon ( 3 points or higher)
     *         false if points are les than 3
     */
    private boolean isValidPolygon(List<Point> points) {
        return points!=null && points.size()>2;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(mPath, mFreeHandDrawer.getPaint());
    }
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        float eventX = event.getX();
        float eventY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(eventX, eventY);
                return true;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(eventX, eventY);
                mPoints.add(new Point((int)eventX,(int)eventY));
                break;
            case MotionEvent.ACTION_UP:

                List<Point> resultPoints = getReducedPolygon(mPoints);

                if (isValidPolygon(resultPoints)) {

                    PolygonOptions polygonOptions = getPolygonOptionsFromPoints(resultPoints);

                    Polygon polygon = mFreeHandDrawer.getMapFragment().getMap().addPolygon(polygonOptions);

                    if (onPolygonDrawListener != null) onPolygonDrawListener.OnDraw(polygon);

                }
                mPoints.clear();
                mPath = new Path();
                break;
            default:
                return false;
        }

        invalidate();
        return true;

    }

    /**
     * @param points List of points will converted to LatLng to create and
     * @return PolygonOptions needed to draw the polygon on map
     */
    private PolygonOptions getPolygonOptionsFromPoints (List<Point> points){
        PolygonOptions polygonOptions = new PolygonOptions()
                .strokeColor(mFreeHandDrawer.getStrokeColor())
                .strokeWidth(mFreeHandDrawer.getStrokeWidth())
                .fillColor(mFreeHandDrawer.getFillColor());

        for (Point p : points) {
            LatLng point= mFreeHandDrawer.getMapFragment().getMap().getProjection().fromScreenLocation(p);
            polygonOptions.add(point);
        }

        return  polygonOptions;
    }

    /**
     *  Reduce the number of points using DouglasPucker algorithm
     *  when tolerance set in mFreeHandDrawer is 0.0 no reduction will be applied
     *  returns the same list in param
     * @param points List<Point> that will be reduced
     * @return List<Point> result of the reduction
     */
    private List<Point> getReducedPolygon(List<Point> points) {
        Double tolerance = mFreeHandDrawer.getTolerance();
        return tolerance==0.0 ?  points : DouglasPeuckerReducer.reduceWithTolerance(points,tolerance);
    }

    public void setLocked(boolean locked) {
        setEnabled(locked);
        setWillNotDraw(locked);
        setFocusable(locked);
        setFocusableInTouchMode(!locked);
    }
}
