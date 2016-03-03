package dev.wolveringer.packet;

import java.util.Random;
import java.util.UUID;

public class MathHelper_v1_8 {
	public static final float a = sqrt(2.0F);
	private static final float[] b = new float[65536];

	static{
		for(int i = 0;i < 65536;i++){
			b[i] = ((float) Math.sin(i * 3.141592653589793D * 2.0D / 65536.0D));
		}
	}

	public static float sin(float paramFloat) {
		return b[((int) (paramFloat * 10430.378F) & 0xFFFF)];
	}

	public static float cos(float paramFloat) {
		return b[((int) (paramFloat * 10430.378F + 16384.0F) & 0xFFFF)];
	}

	public static float sqrt(float paramFloat) {
		return (float) Math.sqrt(paramFloat);
	}

	public static float sqrt(double paramDouble) {
		return (float) Math.sqrt(paramDouble);
	}

	public static int d(float paramFloat) {
		int i = (int) paramFloat;
		return paramFloat < i ? i - 1 : i;
	}

	public static int floor(double paramDouble) {
		int i = (int) paramDouble;
		return paramDouble < i ? i - 1 : i;
	}

	public static float e(float paramFloat) {
		return paramFloat >= 0.0F ? paramFloat : -paramFloat;
	}

	public static int a(int paramInt) {
		return paramInt >= 0 ? paramInt : -paramInt;
	}

	public static int f(float paramFloat) {
		int i = (int) paramFloat;
		return paramFloat > i ? i + 1 : i;
	}

	public static int f(double paramDouble) {
		int i = (int) paramDouble;
		return paramDouble > i ? i + 1 : i;
	}

	public static int clamp(int paramInt1, int paramInt2, int paramInt3) {
		if(paramInt1 < paramInt2){
			return paramInt2;
		}
		if(paramInt1 > paramInt3){
			return paramInt3;
		}
		return paramInt1;
	}

	public static float a(float paramFloat1, float paramFloat2, float paramFloat3) {
		if(paramFloat1 < paramFloat2){
			return paramFloat2;
		}
		if(paramFloat1 > paramFloat3){
			return paramFloat3;
		}
		return paramFloat1;
	}

	public static double a(double paramDouble1, double paramDouble2, double paramDouble3) {
		if(paramDouble1 < paramDouble2){
			return paramDouble2;
		}
		if(paramDouble1 > paramDouble3){
			return paramDouble3;
		}
		return paramDouble1;
	}

	public static double b(double paramDouble1, double paramDouble2, double paramDouble3) {
		if(paramDouble3 < 0.0D){
			return paramDouble1;
		}
		if(paramDouble3 > 1.0D){
			return paramDouble2;
		}
		return paramDouble1 + (paramDouble2 - paramDouble1) * paramDouble3;
	}

	public static double a(double paramDouble1, double paramDouble2) {
		if(paramDouble1 < 0.0D){
			paramDouble1 = -paramDouble1;
		}
		if(paramDouble2 < 0.0D){
			paramDouble2 = -paramDouble2;
		}
		return paramDouble1 > paramDouble2 ? paramDouble1 : paramDouble2;
	}

	public static int nextInt(Random paramRandom, int paramInt1, int paramInt2) {
		if(paramInt1 >= paramInt2){
			return paramInt1;
		}
		return paramRandom.nextInt(paramInt2 - paramInt1 + 1) + paramInt1;
	}

	public static float a(Random paramRandom, float paramFloat1, float paramFloat2) {
		if(paramFloat1 >= paramFloat2){
			return paramFloat1;
		}
		return paramRandom.nextFloat() * (paramFloat2 - paramFloat1) + paramFloat1;
	}

	public static double a(Random paramRandom, double paramDouble1, double paramDouble2) {
		if(paramDouble1 >= paramDouble2){
			return paramDouble1;
		}
		return paramRandom.nextDouble() * (paramDouble2 - paramDouble1) + paramDouble1;
	}

	public static double a(long[] paramArrayOfLong) {
		long l1 = 0L;
		for(long l2 : paramArrayOfLong){
			l1 += l2;
		}
		return l1 / paramArrayOfLong.length;
	}

	public static float degress180(float paramFloat) {
		paramFloat %= 360.0F;
		if(paramFloat >= 180.0F){
			paramFloat -= 360.0F;
		}
		if(paramFloat < -180.0F){
			paramFloat += 360.0F;
		}
		return paramFloat;
	}

	public static double degress180(double paramDouble) {
		paramDouble %= 360.0D;
		if(paramDouble >= 180.0D){
			paramDouble -= 360.0D;
		}
		if(paramDouble < -180.0D){
			paramDouble += 360.0D;
		}
		return paramDouble;
	}

	public static int a(String paramString, int paramInt) {
		try{
			return Integer.parseInt(paramString);
		}catch (Throwable localThrowable){
		}
		return paramInt;
	}

	public static int a(String paramString, int paramInt1, int paramInt2) {
		return Math.max(paramInt2, a(paramString, paramInt1));
	}

	public static double a(String paramString, double paramDouble) {
		try{
			return Double.parseDouble(paramString);
		}catch (Throwable localThrowable){
		}
		return paramDouble;
	}

	public static double a(String paramString, double paramDouble1, double paramDouble2) {
		return Math.max(paramDouble2, a(paramString, paramDouble1));
	}

	public static int b(int paramInt) {
		int i = paramInt - 1;
		i |= i >> 1;
		i |= i >> 2;
		i |= i >> 4;
		i |= i >> 8;
		i |= i >> 16;
		return i + 1;
	}

	private static boolean d(int paramInt) {
		return (paramInt != 0) && ((paramInt & paramInt - 1) == 0);
	}

	private static final int[] c = { 0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9 };

	private static int e(int paramInt) {
		paramInt = d(paramInt) ? paramInt : b(paramInt);
		return c[((int) (paramInt * 125613361L >> 27) & 0x1F)];
	}

	public static int c(int paramInt) {
		return e(paramInt) - (d(paramInt) ? 0 : 1);
	}

	public static int c(int paramInt1, int paramInt2) {
		if(paramInt2 == 0){
			return 0;
		}
		if(paramInt1 == 0){
			return paramInt2;
		}
		if(paramInt1 < 0){
			paramInt2 *= -1;
		}
		int i = paramInt1 % paramInt2;
		if(i == 0){
			return paramInt1;
		}
		return paramInt1 + paramInt2 - i;
	}

	public static UUID a(Random paramRandom) {
		long l1 = paramRandom.nextLong() & 0xFFFF0FFF | 0x4000;
		long l2 = paramRandom.nextLong() & 0xFFFFFFFF | 0x0;
		return new UUID(l1, l2);
	}
}
