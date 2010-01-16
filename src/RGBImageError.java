
public class RGBImageError {

	// root mean squared error
	public static double error(RGBImage img1, RGBImage img2){
		short[][] red1 = img1.getRed();
		short[][] green1 = img1.getGreen();
		short[][] blue1 = img1.getBlue();
		short[][] red2 = img2.getRed();
		short[][] green2 = img2.getGreen();
		short[][] blue2 = img2.getBlue();
		double meanSquaredError = 0.0;
		double massSoFar = 0.0;
		for(int i=0;i<red1.length;i++){
			for(int j=0;j<red1[i].length;j++){
				double dR = red1[i][j] - red2[i][j];
				double dG = green1[i][j] - green2[i][j];
				double dB = blue1[i][j] - blue2[i][j];
				double error = Math.sqrt(dR*dR+dG*dG+dB*dB);
				meanSquaredError = (massSoFar * meanSquaredError + error*error)/(massSoFar+1);
				massSoFar++;
			}
			//System.out.println(meanSquaredError);
		}
		return meanSquaredError;
	}
	
}
