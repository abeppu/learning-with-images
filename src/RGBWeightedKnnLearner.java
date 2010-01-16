import java.util.HashMap;
import java.util.List;

import edu.wlu.cs.levy.CG.KDTree;
import edu.wlu.cs.levy.CG.KeyDuplicateException;
import edu.wlu.cs.levy.CG.KeySizeException;


public class RGBWeightedKnnLearner implements RGBLearner {
	
	
	@Override
	public RGBImage infer(int rowdim, int columndim,
			HashMap<Location, RGBValue> samples) {
		
		/* insert everything into KD tree */
		KDTree<Pair<Location, RGBValue>> sampleTree = new KDTree<Pair<Location, RGBValue>>(2);
		for (Location location : samples.keySet()) {
			// for (RGBValue sample : samples) {
			try {
				double[] key = { location.getRow() + 0.0,
						location.getColumn() + 0.0 };
				sampleTree.insert(key, new Pair<Location, RGBValue>(location, samples.get(location)));
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
						List<Pair<Location, RGBValue>> values = sampleTree.nearest(key, 3);
						RGBValue value = interpolate(location, values);
						red[row][column] = value.r;
						green[row][column] = value.g;
						blue[row][column] = value.b;
					} catch (KeySizeException e) {
						// skip
					}
				}
//			}
		}
		return new RGBImage(red, green, blue);
	}

	public double distance(Location l, Location m){
		double dc= (l.getColumn()-m.getColumn());
		double rc = (l.getRow() - m.getRow());
		return Math.sqrt(dc*dc + rc*rc);
	}
	
	public RGBValue interpolate(Location location, List<Pair<Location, RGBValue>> locationValues){
		double sumSoFar = 0.0;
		double r = 0;
		double g = 0;
		double b = 0;
		for(Pair<Location, RGBValue> locationValue : locationValues){
			double d = distance(location, locationValue.getFirst());
			//d *= d;
			if(d == 0.0) return locationValue.getSecond();
			double w = 1.0/Math.log(d+0.5);
			RGBValue value = locationValue.getSecond();
			r = (r*sumSoFar + value.r*w)/(sumSoFar + w);
			g = (g*sumSoFar + value.g*w)/(sumSoFar + w);
			b = (b*sumSoFar + value.b*w)/(sumSoFar + w);
			sumSoFar += w;
		}
		return new RGBValue((short)r, (short)g, (short)b);
	}
	
	
	public static void main(String[] args) throws Exception {
		double p = 0.1;
		for (int i = 0; i < 8; i++) {
			String filename = "/home/aaron/samplepicture-smaller.ppm";
			RGBImage image = PPMReadWrite.read(filename);
			HashMap<Location, RGBValue> samples = RGBSampler.sample(image, p);
			RGBWeightedKnnLearner learner = new RGBWeightedKnnLearner();
			RGBImage sampleImage = RGBImageFromSample.imageFromSample(
					image.numrows, image.numcolumns, samples);
			String sampleName = "/home/aaron/invlogsmooth-sampledpicture"+i+".ppm";
			PPMReadWrite.write(sampleImage, sampleName);
			RGBImage learnedImage = learner.infer(image.numrows,
					image.numcolumns, samples);
			String outputName = "/home/aaron/invlogsmooth-learnedpicture-"+i+".ppm";
			PPMReadWrite.write(learnedImage, outputName);
			System.out.println(""+samples.size()+"\t"+RGBImageError.error(image, learnedImage));
			
			p*=0.5;
		}

	}
	
}
