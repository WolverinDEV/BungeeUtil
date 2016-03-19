package dev.wolveringer.util;

import java.math.BigDecimal;

public class MathUtil {
	private static final BigDecimal PERCENT_MULTIPLYER = new BigDecimal(100);
	
	public static float calculatePercent(int c, int max) {
		BigDecimal bc = new BigDecimal(c);
		BigDecimal bmax = new BigDecimal(max);
		BigDecimal temp = bc.divide(bmax, 17, BigDecimal.ROUND_HALF_UP);
		temp = temp.multiply(PERCENT_MULTIPLYER);
		return temp.floatValue();
	}
	
	public static void main(String[] args) {
		System.out.println(calculatePercent(2310, 23423));
		System.out.println("X: " + pitchNormalizer(-190));
	}
	
	public static float pitchNormalizer(float pitch) {
		pitch %= 360.0F;
		if (pitch >= 180.0F) {
			pitch -= 360.0F;
		}
		if (pitch < -180.0F) {
			pitch += 360.0F;
		}
		return pitch;
	}
}
