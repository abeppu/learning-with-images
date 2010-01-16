
public class RGBImage {

	int numrows;
	int numcolumns;
	
	short[][] myR;
	short[][] myG;
	short[][] myB;
	
	public RGBImage(short[][] red, short[][] green, short[][] blue){
		myR = red;
		myG = green;
		myB = blue;
		numrows = red.length;
		numcolumns = red[0].length;
	}

	public int getNumrows() {
		return numrows;
	}

	public void setNumrows(int numrows) {
		this.numrows = numrows;
	}

	public int getNumcolumns() {
		return numcolumns;
	}

	public void setNumcolumns(int numcolumns) {
		this.numcolumns = numcolumns;
	}

	public short[][] getRed() {
		return myR;
	}

	public void setRed(short[][] red) {
		this.myR = red;
	}

	public short[][] getGreen() {
		return myG;
	}

	public void setGreen(short[][] green) {
		this.myG = green;
	}

	public short[][] getBlue() {
		return myB;
	}

	public void setBlue(short[][] blue) {
		this.myB = blue;
	}
	
}
