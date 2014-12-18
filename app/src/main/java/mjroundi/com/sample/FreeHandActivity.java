package mjroundi.com.sample;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.maps.model.Polygon;
import com.mjroundi.FreeHandPolygonLib.FreeHandDrawer;
import com.google.android.gms.maps.MapFragment;
import com.mjroundi.FreeHandPolygonLib.OnPolygonDrawListener;

public class FreeHandActivity extends Activity {
    private FreeHandDrawer mFreeHandDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_free_hand);

        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment));
        //Creating the drawer simply
        mFreeHandDrawer =  new FreeHandDrawer.Builder(mapFragment).build();
        // or if you want a custom drawer
/*        mFreeHandDrawer = new FreeHandDrawer
                .Builder(mapFragment)
                .tolerance(0.0)//tolerance needed to reduce number of points default value 0.0 (no reduction)
                .fillColor(0x220000FF)//color used to fill your polygon 0x220000FF is the default value
                .lockZoomWhenDrawing(false)//when true disable zooming of your MapFragment (Only in draw mode)
                .strokeColor(Color.BLUE)///color stroke for polygon 0xFF0000FF is the default value
                .strokeWidth(2)//width of the polygon 2 is ths default value
                .build();
*/
        //Set listener OnPolygonDrawListener
        mFreeHandDrawer.setOnPolygonDrawListener(new OnPolygonDrawListener() {
            @Override
            public void OnDraw(Polygon polygon) {
                Toast.makeText(FreeHandActivity.this,
                                "Polygon contains "+polygon.getPoints().size()+" Points",
                                Toast.LENGTH_LONG)
                                .show();
            }
        });
        //Toggle for switching to drawing mode
        ToggleButton tbDraw = (ToggleButton) findViewById(R.id.tbDraw);
        tbDraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mFreeHandDrawer.setDrawMode(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.free_hand, menu);
        return true;
    }

    public void showTolerancePicker(MenuItem item) {

        final Dialog dialog = new Dialog(FreeHandActivity.this);
        dialog.setTitle("DouglasPeucker Tolerance");
        dialog.setContentView(R.layout.dialog_tolerance);
        Button buttonSet = (Button) dialog.findViewById(R.id.button_set);
        Button buttonCancel = (Button) dialog.findViewById(R.id.button_cancel);
        final NumberPicker numberPickerTolerance = (NumberPicker) dialog.findViewById(R.id.numberPicker_tolerance);
        numberPickerTolerance.setMaxValue(10);
        numberPickerTolerance.setMinValue(0);

        numberPickerTolerance.setValue(mFreeHandDrawer.getTolerance().intValue());

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFreeHandDrawer.setDouglasPeuckerTolerance((double)numberPickerTolerance.getValue());
                dialog.dismiss();
            }
        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }
}
