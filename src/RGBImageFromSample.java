import java.util.HashMap;


public class RGBImageFromSample {

	public static RGBImage imageFromSample(int rowdim, int columndim, HashMap<Location, RGBValue> samples){
		short[][] red = new short[rowdim][columndim];
		short[][] green = new short[rowdim][columndim];
		short[][] blue = new short[rowdim][columndim];
		for(int row=0;row<rowdim;row++){
			for(int column=0;column<columndim;column++){
				Location location = new Location(row, column);
				if(samples.containsKey(location)){
					RGBValue value = samples.get(location);
					red[row][column] = value.r;
					green[row][column] = value.g;
					blue[row][column] = value.b;
				//	System.out.println("recording ("+row+","+column+") -> "+value.toString());
				} else {
					red[row][column] = 100;
					green[row][column] = 100;
					blue[row][column] = 100;
				}
			}
		}
		return new RGBImage(red, green, blue);
	}

	
}
