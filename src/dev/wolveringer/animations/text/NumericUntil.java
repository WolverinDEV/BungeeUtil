package dev.wolveringer.animations.text;

public class NumericUntil {
	public static long ggt(long a,long b){
		if(a > b){
			long h = a;
			a = b;
			b = h;
		}
		long r;
		while (a > 0){
			r = b % a;
			b = a;
			a = r;
		}
		return b;
	}
	public static long ggt(long...z){
		if(z.length>2){
			long nenner = ggt(z[0],z[1]);
			for(int i = 2;i<z.length;i++)
				nenner= ggt(nenner,z[i]);
			return nenner;
		}
		else if(z.length==2)
			return ggt(z[0],z[1]);
		else if(z.length==1)
			return z[0];
		return 1;
	}
}
