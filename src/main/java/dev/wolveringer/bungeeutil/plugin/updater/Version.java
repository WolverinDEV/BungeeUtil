package dev.wolveringer.bungeeutil.plugin.updater;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
@Getter
public class Version implements Comparable<Version>{
	private final String version;

	@Override
	public int compareTo(Version o) {
		String[] ownSegments = this.getPlainVersion().split("\\.");
		String[] otherSegments = o.getPlainVersion().split("\\.");

		for(int i = 0;i<Math.max(ownSegments.length, otherSegments.length);i++){
			if(ownSegments.length <= i){
				if(otherSegments.length <= i) {
					return Boolean.compare(this.isSnapshot(), o.isSnapshot());
				}
				for(int j = i; j < otherSegments.length; j++) {
					if(Integer.parseInt(otherSegments[j]) > 0) {
						return -1;
					}
				}
			} else if(otherSegments.length <= i){
				if(ownSegments.length <= i) {
					return Boolean.compare(this.isSnapshot(), o.isSnapshot());
				}
				for(int j = i; j < ownSegments.length; j++) {
					if(Integer.parseInt(ownSegments[j]) > 0) {
						return 1;
					}
				}
			} else {
				Integer a = Integer.parseInt(ownSegments[i]);
				Integer b = Integer.parseInt(otherSegments[i]);
				if(a > b) {
					return 1;
				} else if(a == b) {
					continue;
				} else if(b > a) {
					return -1;
				} else {
					throw new RuntimeException("LOL this is impossible.");
				}
			}
		}
		return 0;
	}

	public String getPlainVersion(){
		int index = 0;
		while (this.version.length() > index) {
			if(StringUtils.isNumeric(Character.toString(this.version.charAt(index))) || this.version.charAt(index) == '.') {
				index++;
			} else {
				break;
			}
		}
		return this.version.substring(0, index);
	}

	public boolean isSnapshot(){
		return this.version.toLowerCase().endsWith("SNAPSHOT".toLowerCase());
	}

}