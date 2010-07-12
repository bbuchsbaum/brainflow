package brainflow.image.operations;


import java.util.LinkedList;

import brainflow.image.data.IImageData3D;
import brainflow.image.data.IMaskedData3D;
import brainflow.image.data.ImageBuffer3D;
import brainflow.image.iterators.ValueIterator;
import brainflow.image.space.Axis;
import brainflow.math.Index3D;
import cern.colt.list.IntArrayList;


public class LabelComponents {

	/**Mask for the provided brain data. Used to indicate which voxels 
	 * belong to the background and which ones belong to the foreground */
	private IMaskedData3D mask;
	/**Datagrid storing the label values */
	private IImageData3D labelVolume;
	/**Writer used for setting values in the volume */
	private ImageBuffer3D writer;

	/**Stores the size of each labelled cluster */
	private IntArrayList clusterSizes;
	/**The current size of a cluster being labelled */
	private int currentSizeCluster;

	/**The size of the sub-volume used in the iterations */
	private int subSizeRadius;

	/**Iterates the brain data */
	private ValueIterator iter;

	/**Linked lsit containing the newly labelled voxels */
	private LinkedList <Index3D> newVoxelsBuffer;

	/**Current label file */
	private int label;
	/**Starting label file */
	private int startingLabel;

	/**x, y, z dimensions of the image */
	private int x, y, z;
	
	/**List storing the indeces for a specified cluster */
	private IntArrayList cellNeighbours;
	/**The label of a current cluster being retrieved from a 
	 * labelled volume */
	private int clusterLabel;


	/**--------Class constructor------------------ */
	public LabelComponents(IMaskedData3D mask, ImageBuffer3D writer,
			int subSize, int label) {

		this.mask = mask;
		
		//Set the starting label volume
		this.label = label; 
		this.startingLabel = label;

		//Set the size of the sub-volume
		this.subSizeRadius = (subSize-1)/2;

		//Initialize the unlabelled volume
		this.writer = writer;
		labelVolume = writer;

		iter = labelVolume.valueIterator();
		newVoxelsBuffer = new LinkedList<Index3D>();

		//Initialize the hashtable for storing cluster sizes
		clusterSizes = new IntArrayList();
		currentSizeCluster = 0;

		//Set the values for the dimensions
		x = labelVolume.getDimension(Axis.X_AXIS);
		y = labelVolume.getDimension(Axis.Y_AXIS);
		z = labelVolume.getDimension(Axis.Z_AXIS);

	}

	/**If the volume had been labelled in seperate threads, this method merges
	 * the labelledd halfs together */
	public void mergeLabelledSubVolumes(int numberSubVolumesToMerge){

	  /**Case when the volume was labelled by two seperate threads */
	  if(numberSubVolumesToMerge == 2){
	    int i = x/2;
		for(int j = 0; j < y; j++){
		  for(int k = 0; k < z; k++){
			  
		    int index = (x/2) + (j*x) + (k*x*y);
		    int clusterLabel1 = (int)labelVolume.value(index);
		    int clusterLabel2 = (int)labelVolume.value(index+1);
		    
			if(clusterLabel1 != clusterLabel2 &&
					clusterLabel1 > 0 && clusterLabel2 > 0){
			  
				/**TODO: Update the clusterSizes based on the implementation */
				
			}
			
		  }
		}
	  }
	}
	
	/**Given an index, label the cluster containing that index with the
	 * given label */
	public void labelCluster(int clusterLabel, int index){

		/**Clear the linked list for the use of this algorithm */
		newVoxelsBuffer.clear();
		newVoxelsBuffer.push(mask.indexToGrid(index));
		
		/**Use iteration and recursion to get all the cells 
		 * within the cluster */
		while(newVoxelsBuffer.size() > 0){
			Index3D center = newVoxelsBuffer.pop();
			labelClusterRecursive(clusterLabel, center, center);
		}		
	}
	
	/**Run the labelling components algorithm */
	public void labelComponents(){
		while(iter.hasNext()) {
			//Iterate through all voxels in the volume - This should be done
			//once for making sure all voxels become labelled
			int index = iter.index() +1;


			//If an unlabelled voxel is found add it onto the stack
			if(iter.next() < startingLabel && mask.isTrue(index)) {
                Index3D tempIndex3D = mask.indexToGrid(index);
				newVoxelsBuffer.push(tempIndex3D);

				//Continue to recurse through sub-volumes for label voxels
				//until the queue is empty
				while(!newVoxelsBuffer.isEmpty()){
					Index3D center = newVoxelsBuffer.pop();
					labelRecursive(center, center);
				}

				//Increment the label for the next unlabelled component
				//and reset the size for the labelling of the next cluster
				clusterSizes.add(currentSizeCluster);
				label++;
				currentSizeCluster = 0;
			}      
		}

	}

	/**Given an index, return the corresponding cell cluster */
 	public IntArrayList getNeighbourCluster(int index){

		/**List for storing the indexes of the cells in the cluster containing
		 * the given index */
		cellNeighbours = new IntArrayList();

		/**The label of the cluster */
		clusterLabel = (int)writer.value(index);
		
		/**Clear the linked list for the use of this algorithm */
		newVoxelsBuffer.clear();
		newVoxelsBuffer.push(mask.indexToGrid(index));
		
		/**Use iteration and recursion to get all the cells 
		 * within the cluster */
		while(newVoxelsBuffer.size() > 0){
			Index3D center = newVoxelsBuffer.pop();
			getNeighbouringCells(center, center);
		}
		return cellNeighbours;
	}

	/**Method for returning the labelled volume */
	public IImageData3D getLabelledComponents() {
		return labelVolume;
	}

	/**Returns the volume containing the size of each
	 * cluster */
	public IImageData3D getClusterSize(ImageBuffer3D cluster_size_writer){

		IImageData3D clusterSize = cluster_size_writer;

		ValueIterator iter = clusterSize.valueIterator();
		int label;
		do{
			iter.next();
            label = (int)labelVolume.value(iter.index());

			if(label > 0){
				cluster_size_writer.setValue(iter.index(), 
						clusterSizes.get(label-1));
			}

		} while(iter.hasNext());
		return clusterSize;
	}

	/**Method used for labelling a voxel */
	private void label(Index3D index){
		writer.setValue(index.i1(), index.i2(), index.i3(), label);
	}
	
	/**Method used for getting the label for a voxel */
	private int getLabel(Index3D index){
		return (int)writer.value(index.i1(), index.i2(), index.i3());
	}

	/**Method for checking if a voxel is in the boundary of a sub-volume */
	private boolean checkSubBoundary(Index3D idx, Index3D center){
		if(idx.i1() - center.i1() <= subSizeRadius &&
				idx.i2() - center.i2() <= subSizeRadius &&
				idx.i3() - center.i3() <= subSizeRadius &&
				center.i1() - idx.i1() <= subSizeRadius &&
				center.i2() - idx.i2() <= subSizeRadius &&
				center.i3() - idx.i3() <= subSizeRadius 

		) {
			return true;
		} else {
			return false;
		}
	}

	/**Method used to label the components in a sub-volume */
	private void labelRecursive(Index3D current, Index3D center){

		//Add a voxel to the linked list if it belongs to the
		//foreground and is unlabelled
		if((getLabel(current) < startingLabel)
				&& mask.isTrue(current.i1(),current.i2(),current.i3())){

			if(!current.equals(center)){
				newVoxelsBuffer.push(current);
			}

			//Spread recursively from the voxel if it exists within the 
			//boundary of the center voxel and the image itself
			if(checkSubBoundary(current, center)){

				label(current);

				//Increment the iterator if the next voxel already got
				//labelled
				if (current.i1()*x + current.i2()*y + 
						current.i3()*z == iter.index()+2) {
					iter.next();
				}

				currentSizeCluster++;

				//Check for all 26-neighbour voxels reachable from the current 
				//voxel
				for(int i = current.i1()-1; i < x 
				&& i >=0 && i <= current.i1() + 1; i++){
					for(int j = current.i2()-1; j < y 
					&& j >=0 && j <= current.i2()+1; j++){
						for(int k = current.i3()-1; k < z 
						&& k >=0 && k<=current.i3()+1; k++) {							
							labelRecursive(new Index3D(i, j, k), center);
						} 	} 	} 	} 
		}
	}
	
	/**Helper method for searching the corresponding cells that are
	 * part of the given cluster */
	private void getNeighbouringCells(Index3D current, Index3D center){
		
		int integerIndexForm = current.i1() + current.i2()*x + current.i3()*x*y;
		
		/**Check if the voxel is part of the cluster */
		if((getLabel(current) == clusterLabel) && 
				!cellNeighbours.contains(integerIndexForm)){
			
			if(!current.equals(center)){
				newVoxelsBuffer.push(current);
			}
			
			/**Spread recursively from the voxel if it exists within the 
			 * boundary of the center voxel */
			if(checkSubBoundary(current, center)){
				cellNeighbours.add(integerIndexForm);
				
				/**Check for all 26-neighbour voxels reachable from the 
				 * current voxel */
				for(int i = current.i1()-1; i < x 
				&& i >=0 && i <= current.i1() + 1; i++){
					for(int j = current.i2()-1; j < y 
					&& j >=0 && j <= current.i2()+1; j++){
						for(int k = current.i3()-1; k < z 
						&& k >=0 && k<=current.i3()+1; k++) {	
							getNeighbouringCells(new Index3D(i, j, k), center);
						} 	
					} 	
				} 	
			}	
		}	
	}
	
	/**Helper function for labelling a cluster specified by an index */
	private void labelClusterRecursive(int clusterLabel, Index3D current, 
			Index3D center){
		
		/**Check if the voxel has already been labelled with the same file
		 * as the corresponding voxels on the other half of the volume */
		if(!(getLabel(current) == clusterLabel)){
			
			if(!current.equals(center)){
				newVoxelsBuffer.push(current);
			}
			
			/**Spread recursively from the voxel if it exists within the 
			 * boundary of the center voxel */
			if(checkSubBoundary(current, center)){
				label(current);
				
				/**Check for all 26-neighbour voxels reachable from the 
				 * current voxel */
				for(int i = current.i1()-1; i < x 
				&& i >= (x/2) + 1 && i <= current.i1() + 1; i++){
					for(int j = current.i2()-1; j < y 
					&& j >=0 && j <= current.i2()+1; j++){
						for(int k = current.i3()-1; k < z 
						&& k >=0 && k<=current.i3()+1; k++) {	
							getNeighbouringCells(new Index3D(i, j, k), center);
						} 	
					} 	
				} 	
			}	
		}	
		
	}
}