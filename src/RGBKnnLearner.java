import java.util.HashMap;
import java.util.List;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;


public class RGBKnnLearner implements RGBLearner {

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
//				if (samples.containsKey(location)) {					
//					RGBValue value = samples.get(location);
//					red[row][column] = value.r;
//					green[row][column] = value.g;
//					blue[row][column] = value.b;
//				} else {
					try {
						double[] key = { location.getRow() + 0.0,
								location.getColumn() + 0.0 };
						List<RGBValue> values = sampleTree.nearest(key, 3);
						RGBValue value = average(values);
						red[row][column] = value.r;
						green[row][column] = value.g;
						blue[row][column] = value.b;
					} catch (KeySizeException e) {
						// skip
					}
//				}
			}
		}
		return new RGBImage(red, green, blue);
	}

	
	public RGBValue average(List<RGBValue> values){
		int count = 0;
		int sumR = 0;
		int sumB = 0;
		int sumG = 0;
		for(RGBValue value : values){
			count ++;
			sumR += value.r;
			sumB += value.b;
			sumG += value.g;
		}
		short r = (short) (sumR/count);
		short g = (short) (sumG/count);
		short b = (short) (sumB/count);
		return new RGBValue(r, g, b);
	}
	
	public static void main(String[] args) throws Exception {
		double p = 0.1;
		for (int i = 0; i < 8; i++) {
			String filename = "/home/aaron/samplepicture-smaller.ppm";
			RGBImage image = PPMReadWrite.read(filename);
			HashMap<Location, RGBValue> samples = RGBSampler.sample(image, p);
			RGBKnnLearner learner = new RGBKnnLearner();
			RGBImage sampleImage = RGBImageFromSample.imageFromSample(
					image.numrows, image.numcolumns, samples);
			String sampleName = "/home/aaron/knn-sampledpicture"+i+".ppm";
			PPMReadWrite.write(sampleImage, sampleName);
			RGBImage learnedImage = learner.infer(image.numrows,
					image.numcolumns, samples);
			String outputName = "/home/aaron/knn-learnedpicture-"+i+".ppm";
			PPMReadWrite.write(learnedImage, outputName);
			System.out.println("given "+samples.size()+" datapoints, RMSE is "+RGBImageError.error(image, learnedImage));
			p*=0.5;
		}

	}
	
}
