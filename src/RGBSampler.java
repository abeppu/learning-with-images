import java.util.HashMap;
import java.util.Random;
import java.util.Set;


public class RGBSampler {

	public static HashMap<Location, RGBValue> sample(RGBImage image, double p){
		Random random = new Random();
		int rowd = image.numrows;
		int columnd = image.numcolumns;
		short[][] red = image.getRed();
		short[][] green = image.getGreen();
		short[][] blue = image.getBlue();
		HashMap<Location, RGBValue> samples = new HashMap<Location, RGBValue>();
		for(int row=0;row<rowd;row++){
			for(int column=0;column<columnd;column++){
				if(random.nextDouble() < p){
					Location location = new Location(row, column);
					RGBValue sampledValue = new RGBValue(red[row][column], green[row][column], blue[row][column]);
					samples.put(location, sampledValue);
				//	System.out.println("sampled ("+row+","+column+") -> "+sampledValue.toString());
				}
			}
		}
		return samples;
	}
	
	public static HashMap<Location, RGBValue> fakeSample(int rowD, int columnD){
		HashMap<Location, RGBValue> samples = new HashMap<Location, RGBValue>();
		samples.put(new Location(5, 15), new RGBValue((short)150, (short)2, (short)2));
		samples.put(new Location(25, 30), new RGBValue((short)2, (short)150, (short)2));
		samples.put(new Location(30, 35), new RGBValue((short)2, (short)2, (short)150));
		return samples;
	}
	
}
