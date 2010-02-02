package com.ericsson.mmaps.example.demo;

import com.ericsson.mmaps.PickListener;

import android.app.Activity;
import android.widget.Toast;


/**
 * This listener is called when objects are picked/selected.
 *
 */
public class DemoPickListener implements PickListener{
	
	
	private Activity activity_;

	public DemoPickListener(Activity activity) {
		this.activity_ = activity;
	}

	/**
	 * Shows description of picked custom object in a separate screen.
	 */
	public void picked(Object obj, int x, int y, Object tool) {
		if (!(obj instanceof DemoCustomSymbol))
			return;
		DemoCustomSymbol symbol = (DemoCustomSymbol) obj;
		System.out.println("success");
		Toast.makeText(activity_, symbol.getDescription(), Toast.LENGTH_SHORT).show();
	}
}
