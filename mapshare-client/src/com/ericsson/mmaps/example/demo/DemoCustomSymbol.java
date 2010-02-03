package com.ericsson.mmaps.example.demo;


import com.ericsson.mmaps.CustomSymbol;
import com.ericsson.mmaps.Image;


/**
 * Adds a description string to a CustomSymbol.
 * To add application info to an object to be displayed on map,
 * implement the CustomObject interface or subclass a class that does.
 */
public class DemoCustomSymbol extends CustomSymbol{
	/**
	 * Description of the custom object
	 */
	private String description_;
	
    /**
     * Creates a DemoCustomSymbol at the given location, using
     * the given symbol image.
     * 
     * @param x initial x-coordinate of the symbol
     * @param y initial y-coordinate of the symbol
     * @param image the symbol image
     */
    public DemoCustomSymbol(int x, int y, Image image) {
        super(x,y,image);
    }
	
	public String getDescription() {
		return description_;
	}

	public void setDescription(String description_) {
		this.description_ = description_;
	}


}
