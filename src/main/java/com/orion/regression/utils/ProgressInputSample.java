package com.orion.regression.utils;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.JLabel;
import javax.swing.ProgressMonitorInputStream;

public class ProgressInputSample {
  public static final int NORMAL = 0;

  public static void main(String args[]) throws Exception {
    int returnValue = NORMAL;
    
      FileInputStream fis = new FileInputStream("C:\\Users\\Orionindia-PC15\\Desktop\\DEVOPS\\SIA sourcecode\\hogan-db2converter\\extracts\\mainframe-Dump\\db2-unload\\FNBN.PE.DCSAC.UNLOAD.D170901.txt");
      JLabel filenameLabel = new JLabel("FNBN.PE.DCSAC.UNLOAD.D170901.txt", JLabel.RIGHT);
      filenameLabel.setForeground(Color.black);
      Object message[] = { "Reading:", filenameLabel };
      ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(null, message, fis);
      InputStreamReader isr = new InputStreamReader(pmis);
      BufferedReader br = new BufferedReader(isr);
      String line;
      while ((line = br.readLine()) != null) {
        System.out.println(line);
        Thread.sleep(5);
      }
      br.close();
    
    // AWT Thread created - must exit
    System.exit(returnValue);
  }
}
