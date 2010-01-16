
public class RGBValue {

	public RGBValue(short r, short g, short b) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
	}

	short r;
	short g;
	short b;
	
	public String toString(){
		return r+","+g+","+b;
	}
	
}
