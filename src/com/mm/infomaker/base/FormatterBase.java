package com.mm.infomaker.base;

/*
 * # URLကက手机版URLကက价格ကက名称ကက图片URL1က……图片URLnကက所属类别1က……所属类别nက（换行）
 */
public interface FormatterBase {
	
	public Element getMessage(String html);
	
	public Element getMessage(String html,String path);
}
