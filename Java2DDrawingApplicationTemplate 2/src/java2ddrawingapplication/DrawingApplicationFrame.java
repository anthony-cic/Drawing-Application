/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;


/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame implements ActionListener
{
    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    
    private final JPanel line1 = new JPanel();
    private final JPanel line2 = new JPanel(); 
    private final JPanel topPanel = new JPanel(); 

    // create the widgets for the firstLine Panel.
    
    private final JLabel label1 = new JLabel("Shape: ");
    private static final String[] comboList = {"Rectangle", "Oval", "Line"};
    private final JComboBox comboBox1 = new JComboBox(comboList);
    private final JButton buttonColor1 = new JButton("1st Color..."); 
    private final JButton buttonColor2 = new JButton("2nd Color..."); 
    private final JButton buttonUndo = new JButton("Undo");
    private final JButton buttonClear = new JButton("Clear"); 
   

    //create the widgets for the secondLine Panel.
    
    private final JLabel label2 = new JLabel("Options: "); 
    private final JCheckBox checkBoxFilled = new JCheckBox("Filled");
    private final JCheckBox checkBoxGradient = new JCheckBox("Use Gradient");
    private final JCheckBox checkBoxDashed = new JCheckBox("Dashed");
    private final JLabel labelWidth = new JLabel("Line Width: "); 
    private final JSpinner spinnerWidth = new JSpinner(); 
    private final JLabel labelDash = new JLabel("Dash Length: "); 
    private final JSpinner spinnerDash = new JSpinner(); 
    
    // Variables for drawPanel.
 
    private final DrawPanel drawPanel = new DrawPanel();
    private MyShapes drawShape;
    ArrayList<MyShapes> shapes = new ArrayList(); 
 
    Color color1 = Color.RED;
    Color color2 = Color.BLUE;
    
    

    // status label
    private final JLabel statusBar = new JLabel();
    
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame()
    {
        super("Java 2D Drawings");
        
        
        // widgets to panels
        // firstLine widgets
        
        line1.add(label1);
        line1.add(comboBox1);
        line1.add(buttonColor1);
        line1.add(buttonColor2);
        line1.add(buttonUndo);
        line1.add(buttonClear);
      

        // secondLine widgets
        
        line2.add(label2);
        line2.add(checkBoxFilled);
        line2.add(checkBoxGradient);
        line2.add(checkBoxDashed);
        line2.add(labelWidth);
        line2.add(spinnerWidth);
        line2.add(labelDash);
        line2.add(spinnerDash);
        
        spinnerWidth.setValue(5);
        spinnerDash.setValue(5); 
        
        
        // add top panel of two panels
        
        topPanel.setLayout(new GridLayout(2,1)); 
        topPanel.add(line1);
        topPanel.add(line2);
        line1.setBackground(Color.CYAN);
        line2.setBackground(Color.CYAN);
        
        // add topPanel to North, drawPanel to Center, and statusLabel to South

        add(topPanel, BorderLayout.NORTH); 
        add(drawPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH); 
        
        //add listeners and event handlers
        buttonColor1.addActionListener(this);
        buttonColor2.addActionListener(this);
        buttonUndo.addActionListener(this); 
        buttonClear.addActionListener(this); 

        
    }
    @Override
    public void actionPerformed(ActionEvent event) { 
        
        if (event.getSource() == buttonColor1) {
            color1 = JColorChooser.showDialog(null,"Choose a color", color1); 
        }
        if (event.getSource() == buttonColor2) {
            color2 = JColorChooser.showDialog(null, "Choose a color", color2);
        }
        if (event.getSource() == buttonUndo) {
            shapes.remove(shapes.size()-1); 
            drawPanel.repaint();
        }
        if (event.getSource() == buttonClear) {
            shapes.clear(); 
            drawPanel.repaint(); 
        }
        
    }  

    // private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {
        Paint paint;
        Stroke stroke; 
        Point point1, point2;
        
        
        public DrawPanel()
        {
            MouseHandler myListener = new MouseHandler();
            addMouseListener(myListener);
            addMouseMotionListener(myListener);

        }

        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            shapes.forEach(shape -> { 
                shape.draw(g2d);
            });

        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            @Override
            public void mousePressed(MouseEvent event)
            {
              
                 
                point1 = event.getPoint();
                point2 = event.getPoint(); 
                
                // STROKE AND PAINT CHECK BOXES 
                
                float[] spinnerLengthValue = {(float) (int) spinnerDash.getValue()};
                int spinnerWidthValue = (Integer) spinnerWidth.getValue(); 

                if (checkBoxDashed.isSelected()) {
                    stroke = new BasicStroke(spinnerWidthValue, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, spinnerLengthValue,0);
                }
                else {
                    stroke = new BasicStroke(spinnerWidthValue, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }
                
                if (checkBoxGradient.isSelected()) {
                    paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);
                }
                else {
                    paint = color1; 
                }
                
                // COMBO BOX SHAPE CHOOSER 
                
                if (comboBox1.getSelectedItem() == "Rectangle" ) {  
                    drawShape = new MyRectangle(point1, point2, paint, stroke, checkBoxFilled.isSelected());
                    shapes.add(drawShape);
                    drawPanel.repaint();
                }
                else if (comboBox1.getSelectedItem() == "Oval") { 
                    drawShape = new MyOval(point1, point2, paint, stroke, checkBoxFilled.isSelected());
                    shapes.add(drawShape);
                    drawPanel.repaint();
                }
                else if (comboBox1.getSelectedItem() == "Line") { 
                    drawShape = new MyLine(point1, point2, paint, stroke);
                    shapes.add(drawShape);
                    drawPanel.repaint();
                }
                String location = String.format("(%d, %d)", event.getX(), event.getY());
                statusBar.setText(location);

            }

            @Override
            public void mouseReleased(MouseEvent event)
            {
                point2 = event.getPoint();
                drawShape.setEndPoint(point2);
               
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                // update recent item from array list 
                // add to beginning of array so 0th, (no searching) 
                // update second point, keep start the same, update end as you get xy coord 
                
                point2 = event.getPoint(); 
                drawShape.setEndPoint(point2);
                
                String location = String.format("(%d, %d)", event.getX(), event.getY());
                statusBar.setText(location);
                repaint(); 
                
                
            }

            @Override
            public void mouseMoved(MouseEvent e)
            {
            point1 = e.getPoint();
            point2 = e.getPoint(); 
            String location = String.format("(%d, %d)", e.getX(), e.getY());
            statusBar.setText(location);

            }
        }

    }
}
