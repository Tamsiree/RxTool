package com.zbar.lib;

/**
 *
 * 描述: zbar调用类
 */
public class ZbarManager {

	static {
		System.loadLibrary("zbar");
/*		System.load("data/data/com.vondear.rxtools/lib/libZBarDecoder.so");
		System.load("data/data/com.vondear.rxtools/lib/libzbar.so");*/
	}

	public native String decode(byte[] data, int width, int height, boolean isCrop, int x, int y, int cwidth, int cheight);
}
