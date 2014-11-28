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
import android.widget.ToggleButton;
import com.mjroundi.FreeHandPolygonLib.FreeHandDrawer;
import com.google.android.gms.maps.MapFragment;

public class FreeHandActivity extends Activity {
    private MapFragment mMapFragment;
    private ToggleButton mtbDraw;
    private FreeHandDrawer mFreeHandDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_free_hand);

        mMapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment));
        //Creating the drawer
        mFreeHandDrawer = new FreeHandDrawer.Builder(mMapFragment).tolerance(0.0)
                .build();
        //Toggle for switching to drawing mode
        mtbDraw = (ToggleButton) findViewById(R.id.tbDraw);
        mtbDraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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
        //numberPickerTolerance.setWrapSelectorWheel(false);

        buttonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFreeHandDrawer = new FreeHandDrawer.Builder(mMapFragment)
                        .tolerance((double)numberPickerTolerance.getValue())
                        .build();
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
