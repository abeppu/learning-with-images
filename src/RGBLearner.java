import java.util.HashMap;


public interface RGBLearner {

	public RGBImage infer(int rowdim, int columndim, HashMap<Location,RGBValue> samples);
	
}
