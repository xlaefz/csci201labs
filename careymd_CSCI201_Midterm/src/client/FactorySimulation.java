package client;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;

import resource.Factory;
import resource.Resource;

public class FactorySimulation {
	
	Factory mFactory;
	
	private ArrayList<FactoryObject> mFObjects;
	private ArrayList<FactoryWorker> mFWorkers;
	private FactoryNode mFNodes[][];
	private Map<String, FactoryNode> mFNodeMap;
	private FactoryTaskBoard mTaskBoard;
	
	//instance constructor
	{
		mFObjects = new ArrayList<FactoryObject>();
		mFWorkers = new ArrayList<FactoryWorker>();
		mFNodeMap = new HashMap<String, FactoryNode>();
	}
	
	FactorySimulation(Factory inFactory, JTable inTable) {
		mFactory = inFactory;
		mFNodes = new FactoryNode[mFactory.getWidth()][mFactory.getHeight()];
		
		//Create the nodes of the factory
		for(int height = 0; height < mFactory.getHeight(); height++) {
			for(int width = 0; width < mFactory.getWidth(); width++) {
				mFNodes[width][height] = new FactoryNode(width,height);
				mFObjects.add(mFNodes[width][height]);
			}
		}
		
		//Link all of the nodes together
		for(FactoryNode[] nodes: mFNodes) {
			for(FactoryNode node : nodes) {
				int x = node.getX();
				int y = node.getY();
				if(x != 0) node.addNeighbor(mFNodes[x-1][y]);
				if(x != mFactory.getWidth()-1) node.addNeighbor(mFNodes[x+1][y]);
				if(y != 0) node.addNeighbor(mFNodes[x][y-1]);
				if(y != mFactory.getHeight()-1) node.addNeighbor(mFNodes[x][y+1]);
			}
		}
		
		//Create the resources
		for(Resource resource : mFactory.getResources()) {
			FactoryResource fr = new FactoryResource(resource);
			mFObjects.add(fr);
			mFNodes[fr.getX()][fr.getY()].setObject(fr);
			mFNodeMap.put(fr.getName(), mFNodes[fr.getX()][fr.getY()]);
		}
		
		//Create the task board
		Point taskBoardLocation = mFactory.getTaskBoardLocation();
		mTaskBoard = new FactoryTaskBoard(inTable,inFactory.getProducts(),taskBoardLocation.x,taskBoardLocation.y);
		mFObjects.add(mTaskBoard);
		mFNodes[taskBoardLocation.x][taskBoardLocation.y].setObject(mTaskBoard);
		mFNodeMap.put("Task Board", mFNodes[taskBoardLocation.x][taskBoardLocation.y]);
		
		//Create the workers, set their first task to look at the task board
		for(int i = 0; i < mFactory.getNumberOfWorkers(); i++) {
			//Start each worker at the first node (upper left corner)
			//Note, may change this, but all factories have an upper left corner(0,0) so it makes sense
			FactoryWorker fw = new FactoryWorker(i, mFNodes[0][0], this);
			mFObjects.add(fw);
			mFWorkers.add(fw);
		}
	}
	
	public void update(double deltaTime) {
		//Update all the objects in the factor that need updating each tick
		for(FactoryObject object : mFWorkers) object.update(deltaTime);
	}
	
	public ArrayList<FactoryObject> getObjects() {
		return mFObjects;
	}
	
	public ArrayList<FactoryWorker> getWorkers() {
		return mFWorkers;
	}
	
	public FactoryNode[][] getNodes() {
		return mFNodes;
	}

	public String getName() {
		return mFactory.getName();
	}

	public double getWidth() {
		return mFactory.getWidth();
	}
	
	public double getHeight() {
		return mFactory.getHeight();
	}

	public FactoryNode getNode(String key) {
		return mFNodeMap.get(key);
	}

	public FactoryTaskBoard getTaskBoard() {
		return mTaskBoard;
	}
	
}
