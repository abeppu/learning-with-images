import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PPMReadWrite {

	
	public static RGBImage read(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename)));
		// deal with header
		String p3 = reader.readLine();
		reader.readLine();
		String dimensions = reader.readLine();
		Pattern dimensionPattern = Pattern.compile("(\\d+) (\\d+)");
		Matcher dimensionMatcher = dimensionPattern.matcher(dimensions);
		String range = reader.readLine();
		if(dimensionMatcher.matches()){
			int numcolumns = Integer.parseInt(dimensionMatcher.group(1));
			int numrows = Integer.parseInt(dimensionMatcher.group(2));
			short[][] r = new short[numrows][numcolumns];
			short[][] g = new short[numrows][numcolumns];
			short[][] b = new short[numrows][numcolumns];
			String line;
			int loc = 0; // will range to rowdim*columndim;
			int row;
			int column;
			while((line = reader.readLine())!=null){
				String[] numbers = line.split("\\s+");
				for(int i=0;i<numbers.length;i++){
					int rawloc = loc / 3;
					row = rawloc / numcolumns;
					column = rawloc % numcolumns;
					int color = loc % 3;
					if(color == 0) {
						r[row][column] = Short.parseShort(numbers[i]);
					}else if( color ==1){
						g[row][column] =  Short.parseShort(numbers[i]);
					} else if (color ==2){
						 b[row][column] =  Short.parseShort(numbers[i]);		
					}
					loc += 1;
				}
				
				} 
			return new RGBImage(r, g, b);
		} else {
			throw new IOException("could not read this; maybe it's not an ascii rgb ppm?"+dimensions);
		}
	}
	
	
	public static RGBImage read2(String filename) throws IOException{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(filename)));
		// deal with header
		String p3 = reader.readLine();
		reader.readLine();
		String dimensions = reader.readLine();
		Pattern dimensionPattern = Pattern.compile("(\\d+) (\\d+)");
		Matcher dimensionMatcher = dimensionPattern.matcher(dimensions);
		String range = reader.readLine();
		if(dimensionMatcher.matches()){
			int numcolumns = Integer.parseInt(dimensionMatcher.group(1));
			int numrows = Integer.parseInt(dimensionMatcher.group(2));
			short[][] r = new short[numcolumns][numrows];
			short[][] g = new short[numcolumns][numrows];
			short[][] b = new short[numcolumns][numrows];
			String line;
			String[] triples;
			for(int row=0;row<numcolumns;row++){
					line = reader.readLine();
					//if(line == null) System.out.println("died with row = "+row+", column = "+column);
					triples = line.split("\\s+");
					if((triples.length % 3)!=0){
						throw new IOException("this is not a rgb ppm file!");
					}
					for(int i=0;i<triples.length;i+=3){
						r[row][i/3] = (short)Integer.parseInt(triples[i]);
						g[row][i/3] = (short)Integer.parseInt(triples[i+1]);
						b[row][i/3] = (short)Integer.parseInt(triples[i+2]);
					}					
			}
			return new RGBImage(r, g, b);
		} else {
			throw new IOException("could not read this; maybe it's not an ascii rgb ppm?"+dimensions);
		}
	}
	
	public static void  write(RGBImage image, String filename) throws IOException{
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		// write header
		int rowdimension = image.numrows;
		int columndimension = image.numcolumns;
		writer.write("P3");
		writer.newLine();
		writer.write(image.numcolumns+" "+image.numrows);
		writer.newLine();
		writer.write("256");
		writer.newLine();
		for(int row=0;row<rowdimension;row++){
			for(int column=0;column<columndimension;column++){
				writer.write(image.getRed()[row][column]+" ");
				writer.write(image.getGreen()[row][column]+" ");
				writer.write(image.getBlue()[row][column]+"");
				if(column < columndimension - 1)writer.write(" ");
			}
			writer.newLine();
		}
		writer.flush();
		writer.close();
	}
	
	public static void main(String[] args) throws Exception{
		RGBImage image = PPMReadWrite.read("/home/aaron/samplepicture-smallest.ppm");
		PPMReadWrite.write(image, "/home/aaron/deep.ppm");
	}
	
	
	
}
