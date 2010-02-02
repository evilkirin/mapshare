package com.ericsson.mmaps.example.demo;


import com.ericsson.mmaps.CustomLayer;
import com.ericsson.mmaps.GeoMap;
import com.ericsson.mmaps.Image;
import com.ericsson.mmaps.MapComponent;
import com.ericsson.mmaps.MapFactory;
import com.ericsson.mmaps.MapStyle;
import com.ericsson.mmaps.MapView;
import com.ericsson.mmaps.tools.CoordinateTool;
import com.ericsson.mmaps.tools.KeyNavigationTool;
import com.ericsson.mmaps.tools.ScaleBarTool;
import com.ericsson.mmaps.tools.TouchNavigationTool;

import android.app.*;
import android.content.Context;
import android.location.*;
import android.os.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.RelativeLayout.LayoutParams;

public class DemoEmmActivity extends Activity {
	
	private MapView mapView_ = null;
	private CustomLayer customLayer_ = null;
	private ZoomControls zoomControls_ = null;
	private static final String DATABASE_KEY = "l2UfV5Dfg5uIkwVRCWsMuPGel4JAdylPS8AwLmPO";
		
    /** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        MapStyle style = new MapStyle();
        style.set(MapStyle.ROAD_COLOR, 0xF0E164);
        style.set(MapStyle.LAYERS, MapStyle.OUTDOOR_LAYERSET);      
        
        // Set the style below to use Open Street Maps.
        style.set(MapStyle.MAP_SOURCE, MapStyle.OPEN_STREET_MAP);

        MapFactory factory = MapFactory.getInstance();

        try {
			mapView_ = factory.createMapView(this, DATABASE_KEY, style);
		} catch (Exception e) {
			e.printStackTrace();
		}

		RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
		mapView_.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		mainLayout.addView(mapView_,0);
		
		// -------------------- ZOOM -------------------------
		zoomControls_ = (ZoomControls) findViewById(R.id.zoomControls);
		zoomControls_.setOnZoomInClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				GeoMap map = mapView_.getMapComponent().getMap();
				int targetScale = (int)((map.getScale() * (long)20) / 100);
				map.setScale(targetScale);
				mapView_.getMapComponent().repaint();
				
				zoomControls_.setIsZoomOutEnabled(true);
				if (map.getScale() == map.getMinScale())
					zoomControls_.setIsZoomInEnabled(false);
			}
		});

		zoomControls_.setOnZoomOutClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				GeoMap map = mapView_.getMapComponent().getMap();
				int targetScale = (int)((map.getScale() * 100L) / 20);
				map.setScale(targetScale);
				mapView_.getMapComponent().repaint();
				
				zoomControls_.setIsZoomInEnabled(true);
				if (map.getScale() == map.getMaxScale())
					zoomControls_.setIsZoomOutEnabled(false);
			}
		});

		// ------------------ END ZOOM -----------------------
		
		// -------------------- TOOLS ------------------------

		TouchNavigationTool touchController = new TouchNavigationTool(getBaseContext(), mapView_, true);
		touchController.activate();
		touchController.setPickListener(new DemoPickListener(this));

		KeyNavigationTool keyController = new KeyNavigationTool((MapComponent) mapView_.getMapComponent(), false);
        keyController.activate();

		mapView_.getMapComponent().addTool(new ScaleBarTool(mapView_.getMapComponent()));
		mapView_.getMapComponent().addTool(new CoordinateTool(mapView_.getMapComponent()));

		// ------------------ END TOOLS ----------------------
		
		// ------------------- CUSTOM LAYER ------------------
		
		customLayer_ = new CustomLayer();
		mapView_.getMap().addLayer(customLayer_);

        // add some application data to be displayed on map
        DemoCustomSymbol symbol = new DemoCustomSymbol(mapView_.getMap().internalValue(11.9633), 
        											   mapView_.getMap().internalValue(57.7062),
        											   Image.createImage(getBaseContext().getResources().getDrawable(R.drawable.icon)));
        symbol.setText("Museum");
        symbol.setDescription("This is G�teborg City Museum. Entry is free.");

        DemoCustomSymbol symbol1 = new DemoCustomSymbol(mapView_.getMap().internalValue(17.9482),
        		                                        mapView_.getMap().internalValue(59.4031),
        												Image.createImage(getBaseContext().getResources().getDrawable(R.drawable.icon)));
        symbol1.setText("Ericsson Research");
        symbol1.setDescription("This is the office of Ericsson Research in Kista, Sweden!");

        customLayer_.add(symbol);
        customLayer_.add(symbol1);

        // --------------- END CUSTOM LAYER-------------------
    }
    
    @Override
    public void onStart()
    {
    	super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        // This is our one standard application action -- inserting a
        // new note into the list.
        menu.add("Locate Me")
                .setShortcut('3', 'a')
                .setIcon(android.R.drawable.ic_menu_compass);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        	case 0:
        	{
        		Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_COARSE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_LOW);

                LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
                
                String provider = locationManager.getBestProvider(criteria, true);
                if (provider == null) {
                	Toast.makeText(this, "Couldn't get a location provider", Toast.LENGTH_SHORT);
                	return super.onOptionsItemSelected(item);
                }
                Location location = locationManager.getLastKnownLocation(provider);
                
                mapView_.getMap().setGeoCenterY(location.getLatitude());
                mapView_.getMap().setGeoCenterX(location.getLongitude());
        	}
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onDestroy()
    {
    	super.onDestroy();
    	if (mapView_ != null) mapView_.close();
        MapFactory.getInstance().close();
    }
}