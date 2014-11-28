package com.mjroundi.FreeHandPolygonLib;

import com.google.android.gms.maps.model.Polygon;

/**
 * Listener on the touch of the drawer  onTouchEvent
 */
public interface OnPolygonDrawListener {
    /**
     * Called when the user stops touching the TouchableLayer
     * and the polygon is drawn on the map
     * @param polygon the polygon drawn on the map fragment
     */
	public void OnDraw(Polygon polygon);
}
