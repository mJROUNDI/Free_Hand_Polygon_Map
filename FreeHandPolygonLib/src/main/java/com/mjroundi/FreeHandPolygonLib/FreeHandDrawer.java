package com.mjroundi.FreeHandPolygonLib;

import android.graphics.Paint;
import android.widget.FrameLayout;
import com.google.android.gms.maps.MapFragment;

/**
 * Contains Draw parameters
 * and injects a touchable Layer to MapFragment
 */
public class FreeHandDrawer {
    /**
     * Tolerance needed for DouglasPeuckerReducer
     */
	private Double douglasPeuckerTolerance;
    /**
     * Stroke color of polygon and it's preview
     */
	private int strokeColor;
    /**
     * Stroke width of polygon and it's preview
     */
	private float strokeWidth;
    /**
     * Stroke color of polygon
     */
	private int fillColor;
    /**
     * when set to true locks the zoom of MapFragment when it's in draw state
     */
	private boolean zoomLockable;
    /**
     * Paint of polygon's preview
     */
	private Paint paint;
    /**
     * MapFragment where polygons are added
     */
	private MapFragment mapFragment;
    /**
     * TouchableLayer added to the MapFragment
     */
    private TouchableLayer mTouchView;

    /**
     * constructor FreeHandDrawer
     * calls injectDrawer
     * @param builder of FreeHandDrawer
     */
	private FreeHandDrawer(final Builder builder) {
		douglasPeuckerTolerance = builder.douglasPeuckerTolerance;
		strokeColor = builder.strokeColor;
		strokeWidth = builder.strokeWidth;
		fillColor = builder.fillColor;
		paint = builder.paint;
		mapFragment = builder.mapFragment;
		zoomLockable = builder.zoomLockable;
		injectDrawer(mapFragment);
	}

    /**
     * MapFragment getter
     * @return MapFragment
     */
	public MapFragment getMapFragment() {
		return mapFragment;
	}

    /**
     * Sets listener on the TouchableLayer
     * @param onPolygonDrawListener the listener on the polygon Draw
     */
	public void setOnPolygonDrawListener(OnPolygonDrawListener onPolygonDrawListener) {
		mTouchView.setOnPolygonDrawListener(onPolygonDrawListener);
	}

    /**
     * Sets the map to draw mode gesture are locked
     * and zoom is locked too only if zoomLockable is true
     * @param drawMode when true map is in draw mode
     */
	public void setDrawMode(boolean drawMode) {
		mTouchView.setLocked(drawMode);
		mapFragment.getMap().getUiSettings().setScrollGesturesEnabled(!drawMode);
		mapFragment.getMap().getUiSettings().setZoomGesturesEnabled(!(drawMode && zoomLockable));
	}

    /**
     * Paint getter
     * @return Paint
     */
	public Paint getPaint() {
		return paint;
	}

    /**
     * Tolerance getter
     * @return Double tolerance
     */
	public Double getTolerance() {
		return douglasPeuckerTolerance;
	}

    /**
     * Stroke color getter
     * @return int color
     */
	public int getStrokeColor() {
        return strokeColor;
    }

    /**
     * stroke color getter
     * @return int width
     */
    public float getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * fill color getter
     * @return int color
     */
    public int getFillColor() {
        return fillColor;
    }

    /**
     * instantiate TouchableLayer and add it to the view of @param mapFragment
     */
    private void injectDrawer(MapFragment mapFragment) {
		mTouchView = new TouchableLayer(mapFragment.getActivity());
		((FrameLayout)mapFragment.getView()).addView(mTouchView);
		mTouchView.setFreeHandDrawer(this);
	}

    /**
     * Builder for FreeHandDrawer
     */
	public static class Builder {
        /**
         * tolerance value needed by DouglasPeuckerReducer
         * default value is 0 witch means that the polygon will not be reduced at all
         */
		private Double douglasPeuckerTolerance = 0.0;
        /**
         * stroke color of polygon default value blue
         */
		private int strokeColor=0xFF0000FF;
        /**
         * stroke width of polygon default value 2
         */
	    private float strokeWidth=2f;
        /**
         * fill color of polygon default value transparent blue
         */
	    private int fillColor=0x220000FF;
        /**
         * paint of the preview stroke
         */
		private Paint paint=new Paint();
        /**
         * MapFragment where polygons are added
         */
		private MapFragment mapFragment;
        /**
         * when set to true locks the zoom of MapFragment when it's in draw state
         * default value false ( zoom is not locked
         */
		private boolean zoomLockable=false;

        /**
         * locks the zoom of MapFragment when it's in draw state
         * @param lock when true zoom is locked
         * @return Builder
         */
		public Builder lockZoomWhenDrawing(boolean lock) {
		    zoomLockable = lock;
            return this;
        }

        /**
         * @param mapFragment the MapFragment in witch polygons will be drawn
         * @return Builder
         */
		public Builder(MapFragment mapFragment) {
			this.mapFragment=mapFragment;
		}

        /**
         * Sets the stroke color of polygon
         * @param lineColor the color of the polygon line
         * @return Builder
         */
		public Builder strokeColor(int lineColor) {
		    strokeColor = lineColor;
            return this;
        }

        /**
         * Sets the stroke width of polygon
         * @param lineWidth the width of the polygon line
         * @return Builder
         */
		public Builder strokeWidth(float lineWidth) {
		    strokeWidth = lineWidth;
            return this;
        }

        /**
         * Sets the fill color of polygon
         * @param fillColor  the fill color of the polygon
         * @return Builder
         */

		public Builder fillColor(int fillColor) {
		    this.fillColor = fillColor;
			return this;
		}

        /**
         * sets the tolerance used by DouglasPeucker reduction algorithm
         * @throws IllegalArgumentException when tolerance is < to 0
         * @param tolerance tolerance used by DouglasPeucker reduction algorithm
         * @return Builder
         */
		public Builder tolerance(Double tolerance) {
            if (tolerance < 0.0) throw new IllegalArgumentException("tolerance must be higher than 0 ");
            douglasPeuckerTolerance = tolerance;
            return this;
        }

        /**
         * Sets a custom Paint
         * @param paint that will be used to draw the preview of the polygon
         * @return Builder
         */
		public Builder pathStyle(Paint paint) {
		    if (paint == null) throw new IllegalArgumentException("polygonOptions must not be null ");
			this.paint = paint;
			return this;
		}

        /**
         * calls initPaint()
         * @return FreeHandDrawer instance
         */
		public FreeHandDrawer build() {
		    initPaint();
			return new FreeHandDrawer(this);
		}

        /**
         * initialisation of the paint used when drawing preview polygon
         */
		private void initPaint() {
		    paint.setAntiAlias(true);
            paint.setStrokeWidth(strokeWidth);
            paint.setColor(strokeColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
        }
	}
}
