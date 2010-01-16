import java.util.HashMap;
import java.util.Set;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;

public class RGBNearestNeighborLearner implements RGBLearner {

	@Override
	public RGBImage infer(int rowdim, int columndim,
			HashMap<Location, RGBValue> samples) {
		/* insert everything into KD tree */
		KDTree<RGBValue> sampleTree = new KDTree<RGBValue>(2);
		for (Location location : samples.keySet()) {
			// for (RGBValue sample : samples) {
			try {
				double[] key = { location.getRow() + 0.0,
						location.getColumn() + 0.0 };
				sampleTree.insert(key, samples.get(location));
			//	System.out.println("loaded ("+key[0]+","+key[1]+") -> "+samples.get(location).toString());
			} catch (KeyDuplicateException e) {
				// skip
			} catch (KeySizeException e) {

			}
		}
		/*
		 * assemble image : -- for each value which already exists in the
		 * sample, leave as is -- for each non-sample value
		 */
		short[][] red = new short[rowdim][columndim];
		short[][] green = new short[rowdim][columndim];
		short[][] blue = new short[rowdim][columndim];
		for (int row = 0; row < rowdim; row++) {
			for (int column = 0; column < columndim; column++) {
				Location location = new Location(row, column);
				if (samples.containsKey(location)) {
					RGBValue value = samples.get(location);
					red[row][column] = value.r;
					green[row][column] = value.g;
					blue[row][column] = value.b;
				} else {
					try {
						double[] key = { location.getRow() + 0.0,
								location.getColumn() + 0.0 };
						RGBValue value = sampleTree.nearest(key);
						//System.out.println("closest to "+key[0]+","+key[1]+" is "+value.toString());
						red[row][column] = value.r;
						green[row][column] = value.g;
						blue[row][column] = value.b;
					} catch (KeySizeException e) {
						// skip
					}
				}
			}
		}
		return new RGBImage(red, green, blue);
	}

	public static void main(String[] args) throws Exception {
		double p = 0.1;
		for (int i = 0; i < 8; i++) {
			String filename = "/home/aaron/samplepicture-smaller.ppm";
			RGBImage image = PPMReadWrite.read(filename);
			HashMap<Location, RGBValue> samples = RGBSampler.sample(image, p);
			RGBNearestNeighborLearner learner = new RGBNearestNeighborLearner();
			RGBImage sampleImage = RGBImageFromSample.imageFromSample(
					image.numrows, image.numcolumns, samples);
			String sampleName = "/home/aaron/sampledpicture"+i+".ppm";
			PPMReadWrite.write(sampleImage, sampleName);
			RGBImage learnedImage = learner.infer(image.numrows,
					image.numcolumns, samples);
			String outputName = "/home/aaron/learnedpicture-"+i+".ppm";
			PPMReadWrite.write(learnedImage, outputName);
			System.out.println("given "+samples.size()+" datapoints, RMSE is "+RGBImageError.error(image, learnedImage));
			p*=0.5;
		}

	}

}
