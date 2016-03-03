package dev.wolveringer.BungeeUtil.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExceptionUtils {
	private static final String class_name;

	static{
		class_name = ExceptionUtils.class.getName();
	}

	public static StackTraceElement[] replaceStackTraceElement(StackTraceElement[] in, StackTraceElement old_stack, StackTraceElement new_stack) {
		List<StackTraceElement> arraylist_new_stack = new ArrayList<StackTraceElement>();
		for(StackTraceElement e : in){
			if(e.getClassName() == old_stack.getClassName())
				if(e.getMethodName() == old_stack.getMethodName())
					if(e.getFileName() == old_stack.getFileName())
						if(new_stack != null)
							arraylist_new_stack.add(new_stack);
						else
							continue;
			arraylist_new_stack.add(e);
		}
		return arraylist_new_stack.toArray(new StackTraceElement[arraylist_new_stack.size()]);
	}

	public static StackTraceElement[] replaceCurruntStackTraceElement(StackTraceElement[] in) {
		return replaceStackTraceElement(in, getCurruntMethodeStackTraceElement(), null);
	}

	public static StackTraceElement getCurruntMethodeStackTraceElement() {
		StackTraceElement[] _currunt = Thread.currentThread().getStackTrace();
		for(StackTraceElement e : _currunt)
			if(e != null && !e.getClassName().equals(class_name) && !e.getMethodName().equalsIgnoreCase("getStackTrace"))
				return e;
		return null;
	}

	public static Throwable replaceCurruntStackTraceElement(Throwable in) {
		return replaceStackTraceElement(in, getCurruntMethodeStackTraceElement(), null);
	}

	public static Throwable replaceStackTraceElement(Throwable in, StackTraceElement old_stack, StackTraceElement new_stack) {
		List<StackTraceElement> arraylist_new_stack = new ArrayList<StackTraceElement>();
		for(StackTraceElement e : in.getStackTrace()){
			if(e.getClassName() == old_stack.getClassName())
				if(e.getMethodName() == old_stack.getMethodName())
					if(e.getFileName() == old_stack.getFileName())
						if(new_stack != null)
							arraylist_new_stack.add(new_stack);
						else
							continue;
			arraylist_new_stack.add(e);
		}
		in.setStackTrace(arraylist_new_stack.toArray(new StackTraceElement[arraylist_new_stack.size()]));
		return in;
	}
	
	public static int getIndexOf(StackTraceElement[] in,StackTraceElement e){
		for(int i = 0;i < in.length;i++){
			StackTraceElement stackTraceElement = in[i];
			if(e.getClassName() == stackTraceElement.getClassName())
				if(e.getMethodName() == stackTraceElement.getMethodName())
					if(e.getFileName() == stackTraceElement.getFileName())
						return i;
		}
		return -1;
	}
	public static int getIndexOf(Throwable ex,StackTraceElement e){
		return getIndexOf(ex.getStackTrace(), e);
	}
	public static int getCurrentMethodeIndex(StackTraceElement[] in){
		return getIndexOf(in, getCurruntMethodeStackTraceElement());
	}
	public static int getCurrentMethodeIndex(Throwable ex){
		return getIndexOf(ex, getCurruntMethodeStackTraceElement());
	}
	public static StackTraceElement[] deleteDownward(StackTraceElement[] ex,int index){
		return Arrays.copyOf(ex, index);
	}
	public static StackTraceElement[] deleteUpward(StackTraceElement[] ex,int index){
		return Arrays.copyOfRange(ex, index, ex.length);
	}
}
