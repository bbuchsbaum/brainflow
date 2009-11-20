package brainflow.image.operations;

import cern.colt.list.IntArrayList;
import brainflow.image.space.Axis;
import brainflow.image.data.IImageData3D;
import brainflow.image.data.ImageBuffer3D;
import brainflow.image.data.IMaskedData3D;

public class ParallelComponentLabeler {

	private IImageData3D labelledVolume;

	private ImageBuffer3D labelledVolumeImageWriter;

	private int x, y, z;

	int subVolumeSize = 10;

	private ComponentLabeler l1;

	public ParallelComponentLabeler(IMaskedData3D imageMask,
			ImageBuffer3D labelledVolumeImageWriter, int numOfThreads){

		this.labelledVolumeImageWriter = labelledVolumeImageWriter;
		labelledVolume = labelledVolumeImageWriter;
		this.x  = labelledVolume.getDimension(Axis.X_AXIS);
		this.y  = labelledVolume.getDimension(Axis.Y_AXIS);
		this.z  = labelledVolume.getDimension(Axis.Z_AXIS);

		if(numOfThreads == 2){
			l1 = new ComponentLabeler(imageMask, this.labelledVolumeImageWriter,
					0, 0, 0, x, y, z/2,
					subVolumeSize, 1);
			ComponentLabeler l2 = new ComponentLabeler(imageMask, this.labelledVolumeImageWriter,
					0, 0, z/2, x, y, z,
					subVolumeSize, 2);
			
			l1.label();
			l2.label();

			while(l1.labelled==false || l2.labelled ==false){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			l1.merge(0, 0, (z/2), x, y, (z/2)+1);
		}
		
		if(numOfThreads == 4){
			l1 = new ComponentLabeler(imageMask, this.labelledVolumeImageWriter,
					0, 0, 0, x, y/2, z/2,
					subVolumeSize, 1);
			ComponentLabeler l2 = new ComponentLabeler(imageMask, this.labelledVolumeImageWriter,
					0, 0, z/2, x, y/2, z,
					subVolumeSize, 2);
			ComponentLabeler l3 = new ComponentLabeler(imageMask, this.labelledVolumeImageWriter,
					0, y/2, 0, x, y, z/2,
					subVolumeSize, 3);
			ComponentLabeler l4 = new ComponentLabeler(imageMask, this.labelledVolumeImageWriter,
					0, y/2, z/2, x, y, z,
					subVolumeSize, 4);

			l1.label();
			l2.label();
			l3.label();
			l4.label();
			while(l1.labelled==false || l2.labelled ==false || l3.labelled ==false || l4.labelled ==false){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			l1.merge(0, 0, z/2, x, y, (z/2)+1);
			l1.merge(0, y/2, 0, x, (y/2)+1, z);

		}
	}

    public IImageData3D getLabel(){
		return labelledVolume;
	}
	public IImageData3D getSizeVolume(){
		return l1.getSizeVolume(labelledVolumeImageWriter);
	}
	public IntArrayList getNeighbourCluster(int index){
		return  l1.getNeighbourCluster(index);
	}
}
