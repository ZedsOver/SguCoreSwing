/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Beans/Bean.java to edit this template
 */
package main_app.swing;

import java.beans.*;
import java.io.Serializable;

/**
 *
 * @author armax
 */
public class Torak implements Serializable {
    
    public static final String P1ROP_SAMPLE_PROPERTY = "sampleProperty";
    
    private String sampleProperty;
    
    private PropertyChangeSupport propertySupport;
    
    public Torak()
    {
        propertySupport = new PropertyChangeSupport(this);
    }
    
    public String getSampleProperty()
    {
        return sampleProperty;
    }
    
    public void setSampleProperty(String value)
    {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(P1ROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener)
    {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener)
    {
        propertySupport.removePropertyChangeListener(listener);
    }
    
}
