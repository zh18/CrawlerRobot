package com.mm.data;

import java.util.List;

import com.mm.data.struct.Element;

/*
 * # URLကက手机版URLကက价格ကက名称ကက图片URL1က……图片URLnကက所属类别1က……所属类别nက（换行）
 */
public interface FormatterBase extends Idata{
	
	public Element getMessage(String html);
	
	public Element getMessage(String html,String path);
}
