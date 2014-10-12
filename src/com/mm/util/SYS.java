package com.mm.util;

import java.io.File;

import com.mm.db.DataBase;

public interface SYS {
	public String SYS_DG_SCHEME = "scheme";
	public String SYS_DG_SCHEME_FLODER="scheme"+File.separator;
	public String SYS_IF_SCHEME = SYS_DG_SCHEME;
	public String SYS_IF_SCHEME_FLODER = SYS_DG_SCHEME_FLODER;
	public String SYS_BIN = "com.mm.server.bins";
	public String SYS_BIN_FLODER="com"+File.separator+"mm"+File.separator+"server"+File.separator+"bins";
	public String PATH = DataBase.getString("syspath");
}
