package org.cytoscape.myappViz.internal.algorithmPanels;


import javax.swing.*;

import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.myappViz.internal.ClusterUtil;
import org.cytoscape.myappViz.internal.CollapsiblePanel;
import org.cytoscape.myappViz.internal.MyTipTool;
import org.cytoscape.myappViz.internal.ParameterSet;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.ArrayList;



public class OHPINpanel extends JPanel{
	private final ClusterUtil mcodeUtil;
	private CySwingApplication desktopApp;
	ParameterSet currentParameters;

    private DecimalFormat decimal; // used in the formatted text fields
	private JFormattedTextField fThreshold;
    private JFormattedTextField OverlappingScore;

	public OHPINpanel(CySwingApplication swingApplication, ClusterUtil mcodeUtil){

		
		desktopApp=swingApplication;
		  this.mcodeUtil = mcodeUtil;

		

		    this.currentParameters =this.mcodeUtil.getCurrentParameters().getParamsCopy(null);
//        currentParameters = ParameterSet.getInstance().getParamsCopy(null);

		decimal = new DecimalFormat();
        decimal.setParseIntegerOnly(true);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createTitledBorder(""));
        //the collapsible panel
        CollapsiblePanel collapsiblePanel = new CollapsiblePanel("OH-PIN Options");
        JPanel cliqueSizePanel=createCliqueSizePanel();
        JPanel OverlappingScorePanel=createOverlappingScorePanel();
        collapsiblePanel.getContentPane().add(cliqueSizePanel, BorderLayout.NORTH);
        collapsiblePanel.getContentPane().add(OverlappingScorePanel, BorderLayout.CENTER);
        collapsiblePanel.setToolTipText("Customize parameters for HC-PIN (Optional)");
        this.add(collapsiblePanel);
	}

    private JPanel createCliqueSizePanel(){
    	JPanel panel;
        //the label
        JLabel sizeThresholdLabel=new JLabel("Threshold");  
        fThreshold = new JFormattedTextField(decimal) {
            public JToolTip createToolTip() {
                return new MyTipTool();
            }
        };
        //the input text field
        fThreshold.setColumns(3);
        fThreshold.addPropertyChangeListener("value", new OHPINpanel.FormattedTextFieldAction());
        String tip = "threshold to define a module\n" +
                "It stands for the proportion of\n"+
                "the indegree to the outdegree of a clique";
        fThreshold.setToolTipText(tip);
        fThreshold.setText((new Double(currentParameters.getfThreshold()).toString()));
        //the panel 
        panel = new JPanel() {
            public JToolTip createToolTip() {
                return new MyTipTool();
            }
        };
        //add the components to the panel
        panel.setLayout(new BorderLayout());
        panel.setToolTipText(tip);
        panel.add(sizeThresholdLabel, BorderLayout.WEST);
        panel.add(fThreshold, BorderLayout.EAST);
    	return panel;
    }

    private JPanel createOverlappingScorePanel(){
        JPanel panel=new JPanel();
        panel.setLayout(new BorderLayout());        
        //the label
        JLabel sizeThresholdLabel2=new JLabel("Overlapping Score");  
        //the input text field
        OverlappingScore= new JFormattedTextField(decimal) {
            public JToolTip createToolTip() {
                return new MyTipTool();
            }
        };
        OverlappingScore.setColumns(3);
        OverlappingScore.addPropertyChangeListener("value", new OHPINpanel.FormattedTextFieldAction());
        String tip3 = "evaluate the overlap of two clusters\n" +
                "usually set to 0.5";
        OverlappingScore.setToolTipText(tip3);
        OverlappingScore.setText((new Double(currentParameters.getOverlappingScore()).toString()));
        panel.add(sizeThresholdLabel2,BorderLayout.WEST);
        panel.add(OverlappingScore,BorderLayout.EAST);
        return panel;
    }

    private class FormattedTextFieldAction implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent e) {
            JFormattedTextField source = (JFormattedTextField) e.getSource();
            String message = "Invaluled input\n";
            boolean invalid = false;
            
            
            currentParameters=mcodeUtil.getMainPanel().getCurrentParamsCopy();// 
            
            
            if (source == fThreshold) {
                Number value = (Number) fThreshold.getValue();
           //     if ((value != null) && (value.doubleValue() > 0.1) && (value.doubleValue() <= 10.0)) {
                    currentParameters.setfThreshold(value.doubleValue());
          //      } else {
                    source.setValue(new Double (currentParameters.getfThreshold()));
          //          message += "module threshold should\n" +
    	//					"be set between 0 and 10.";
        //            invalid = true;
       //         }
            }else if (source == OverlappingScore) {
                Number value = (Number) OverlappingScore.getValue();
                if ((value != null) && (value.doubleValue() >= 0)) {
                    currentParameters.setOverlappingScore(value.doubleValue());
                } else {
                    source.setValue(new Double (currentParameters.getOverlappingScore()));
                    message += "size of output module cutoff should\n" +
    						"be greater than 0.";
                    invalid = true;
                }
			}
            
            if (invalid) {
                JOptionPane.showMessageDialog(desktopApp.getJFrame(), message, "paramter out of boundary", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

}
