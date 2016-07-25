package client;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import resource.Product;

public class WorkerInfoWindow extends JFrame{
	private static final long serialVersionUID = -3026086601539728723L;
	
	private final FactoryWorker mWorker;
	private final JLabel mImageLabel;
	private final JLabel mPositionLabel;
	private final JLabel mProductToMakeLabel;
	private final JLabel mDestinationLabel;
	
	private final JTable mTable;
	private final DefaultTableModel mTableModel;
	
	public WorkerInfoWindow(FactoryWorker inWorker){
		mWorker = inWorker;
		setTitle("Worker Info");
		setSize(320,240);
		setResizable(false);
		
		mImageLabel = new JLabel(new ImageIcon(mWorker.mImage));
		mPositionLabel = new JLabel();
		mProductToMakeLabel = new JLabel();
		mDestinationLabel = new JLabel();
		
		Box leftBox = Box.createVerticalBox();
		leftBox.add(mImageLabel);
		leftBox.add(Box.createVerticalStrut(10));
		leftBox.add(mPositionLabel);
		leftBox.add(Box.createVerticalStrut(10));
		leftBox.add(new JLabel("Current Task:"));
		leftBox.add(mProductToMakeLabel);
		leftBox.add(Box.createVerticalStrut(10));
		leftBox.add(new JLabel("Going to:"));
		leftBox.add(mDestinationLabel);
		leftBox.setBorder(new EmptyBorder(10,10,10,10));
		add(leftBox,"West");
		
		Object columnNames[] = { "Product", "# Complete"};
		Object rows[][] = {};
		mTableModel = new DefaultTableModel(rows,columnNames); //this creates the table
		mTable = new JTable(mTableModel);
		JScrollPane jsp = new JScrollPane(mTable);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				mPositionLabel.setText("Position: ("+mWorker.getX()+","+mWorker.getY()+")");
				if(mWorker.getProductToMake() != null) {
					mProductToMakeLabel.setText(mWorker.getProductToMake().getName());
				} else {
					mProductToMakeLabel.setText("N/A");
				}
				if(mWorker.getDestinationNodeObject() != null) {
					mDestinationLabel.setText(mWorker.getDestinationNodeObject().mLabel);
				} else {
					mDestinationLabel.setText("N/A");
				}
				HashMap<String, Integer> map = new HashMap<String, Integer>();
				for(Product p : mWorker.getProductsComplete()) {
					String name = p.getName();
					if(map.containsKey(name)) map.replace(name, map.get(name)+1);
					else map.put(name, 1);
				}
				mTableModel.setRowCount(0);
				for(Entry<String, Integer> es : map.entrySet()) {
					Object[] row = {es.getKey(), es.getValue()};
					mTableModel.addRow(row);
				}
			}
		});
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JLabel workerLabel = new JLabel(mWorker.mLabel);
		workerLabel.setFont(new Font("",0,24));
		mainPanel.add(workerLabel, "North");
		mainPanel.add(jsp, "Center");
		mainPanel.add(refresh, "South");
		mainPanel.setBorder(new EmptyBorder(10,10,10,10));
		add(mainPanel);
		refresh.doClick(); //this automatically updates on the first click
	}
}
