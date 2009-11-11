package org.pentaho.agilebi.pdi.modeler;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.pentaho.ui.xul.XulDomContainer;
import org.pentaho.ui.xul.XulException;
import org.pentaho.ui.xul.XulRunner;
import org.pentaho.ui.xul.binding.BindingFactory;
import org.pentaho.ui.xul.binding.DefaultBindingFactory;
import org.pentaho.ui.xul.impl.XulEventHandler;
import org.pentaho.ui.xul.swt.SwtXulLoader;
import org.pentaho.ui.xul.swt.SwtXulRunner;

public class XulUI {

  private XulDomContainer container;

  XulRunner runner;
  BindingFactory bf;
  
  public XulUI( Shell shell, XulEventHandler... handlers ){
    try{
      SwtXulLoader loader = new SwtXulLoader();
      loader.setOuterContext(shell);
      container = loader.loadXul("org/pentaho/spike/panel.xul");
      bf = new DefaultBindingFactory();
      bf.setDocument(container.getDocumentRoot());
  
      if(handlers != null){
        for(XulEventHandler h : handlers){
          container.addEventHandler(h);
        }
      }
      
      runner = new SwtXulRunner();
      runner.addContainer(container);
      runner.initialize();
    } catch(Exception e){
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
  }
  
  public Composite getMainPanel(){
    return (Composite) container.getDocumentRoot().getRootElement().getElementById("mainVBox").getManagedObject();
  }
  
  public BindingFactory getBindingFactory() {
    return bf;
  }
  
  public void startDebugWindow(){
    try {
      runner.start();
    } catch (XulException e) {
      e.printStackTrace();
    }
  }
  
  public XulDomContainer getContainer() {
    return container;
  }
  
  public static void main(String[] args){
    new XulUI(null, null).startDebugWindow();
    
  }
  
}