package com.mm.infomaker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mm.infomaker.scheme.AComFImpl;
import com.mm.infomaker.scheme.Amazon2FImpl;
import com.mm.infomaker.scheme.DDFImpl;
import com.mm.infomaker.scheme.EbayFImpl;
import com.mm.infomaker.scheme.JiuMiFImpl;
import com.mm.infomaker.scheme.JiuXianFImpl;
import com.mm.infomaker.scheme.LanQuanFImpl;
import com.mm.infomaker.scheme.TbFImpl;
import com.mm.infomaker.scheme.WMFImpl;
import com.mm.infomaker.scheme.YeMaijiuFImpl;
import com.mm.infomaker.scheme.YhdFImpl;

public class Main implements Serializable{

	
	
	
	Thread tb = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		@Override
		public void run() {
			tblist.add("/home/public/zhanhe/dd/ghhz/taobao/");
			tblist.add("/home/public/zhanhe/dd/spyl/taobao/");
			tblist.add("/home/public/zhanhe/dd/wanju/taobao/");
			tblist.add("/home/public/zhanhe/dd/jiulei/taobao/");
			tblist.add("/home/public/zhanhe/dd/my/taobao/");
			tblist.add("/home/public/zhanhe/dd/yybj/taobao/");
			try {
				util.infoMaker(tblist, new TbFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread dd = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		@Override
		public void run() {
			tblist.add("/home/public/zhanhe/dd/ghhz/dd/");
			tblist.add("/home/public/zhanhe/dd/spyl/dd/");
			tblist.add("/home/public/zhanhe/dd/wanju/dd/");
			tblist.add("/home/public/zhanhe/dd/jiulei/dd/");
			tblist.add("/home/public/zhanhe/dd/my/dd/");
			tblist.add("/home/public/zhanhe/dd/yybj/dd/");
			try {
				util.infoMaker(tblist, new DDFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread amazon = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		@Override
		public void run() {
			tblist.add("/home/public/zhanhe/dd/ghhz/amazon/");
			tblist.add("/home/public/zhanhe/dd/spyl/amazon/");
			tblist.add("/home/public/zhanhe/dd/wanju/amazon/");
			tblist.add("/home/public/zhanhe/dd/jiulei/amazon/");
			tblist.add("/home/public/zhanhe/dd/my/amazon/");
			tblist.add("/home/public/zhanhe/dd/yybj/amazon/");
			try {
				util.infoMaker(tblist, new Amazon2FImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread yhd = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		@Override
		public void run() {
			tblist.add("/home/public/zhanhe/dd/ghhz/yhd/");
			tblist.add("/home/public/zhanhe/dd/spyl/yhd/");
			tblist.add("/home/public/zhanhe/dd/wanju/yhd/");
			tblist.add("/home/public/zhanhe/dd/jiulei/yhd/");
			tblist.add("/home/public/zhanhe/dd/my/yhd/");
			tblist.add("/home/public/zhanhe/dd/yybj/yhd/");
			try {
				util.infoMaker(tblist, new YhdFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread ebay = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		public void run() {
			tblist.add("/home/public/zhanhe/dd/wanju/ebay/");
			tblist.add("/home/public/zhanhe/dd/ghhz/ebay/");
			tblist.add("/home/public/zhanhe/dd/my/ebay/");
			try{
				util.infoMaker(tblist, new EbayFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread amazonCom = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		public void run() {
			tblist.add("/home/public/zhanhe/dd/spyl/amazon.com/");
			tblist.add("/home/public/zhanhe/dd/ghhz/amazon.com/");
			tblist.add("/home/public/zhanhe/dd/jiulei/amazon.com/");
			try{
				util.infoMaker(tblist, new AComFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread walmart = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		public void run() {
			tblist.add("/home/public/zhanhe/dd/ghhz/walmart/");
			tblist.add("/home/public/zhanhe/dd/my/walmart/");
			tblist.add("/home/public/zhanhe/dd/jiulei/walmart/");
			try{
				util.infoMaker(tblist, new WMFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread yemaijiu = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		public void run() {
			tblist.add("/home/public/zhanhe/dd/jiulei/yemaijiu/");
			try{
				util.infoMaker(tblist, new YeMaijiuFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	
	Thread jiuxian = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		public void run() {
			tblist.add("/home/public/zhanhe/dd/jiulei/jiuxian/");
			try{
				util.infoMaker(tblist, new JiuXianFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	
	Thread lanquan = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		public void run() {
			tblist.add("/home/public/zhanhe/dd/book/lanquan/");
			try{
				util.infoMaker(tblist, new LanQuanFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
	
	Thread jiumi = new Thread(new Runnable() {
		List<String> tblist = new ArrayList<String>();
		Util util = new Util();
		public void run() {
			tblist.add("/home/public/zhanhe/dd/jiulei/jiumi/");
			try{
				util.infoMaker(tblist, new JiuMiFImpl());
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	});
}
