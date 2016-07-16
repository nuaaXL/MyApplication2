package com.example.chenjunfan.myapplication;

public class Request {
	int num;//编号
	String time;//日期
	int flag; //1寄      2取
	String publisher,//求助人
			p_number, //求助人学号
			p_phone,//求助人联系方式
			helper,//接单人
			h_number,//接单人学号
			h_phone,//接单人联系方式
			user_loc;//求助人位置
	String content,//包裹内容
			infor,//备注
			r_nameORmessage, //1收件人姓名      2取件短信
			r_locORpackage_loc,//1收件人位置     2包裹位置
			r_phoneORphone,  //1收件人联系方式         2取件手机号码
			nullORpackage_Id;//1无       2快递号

	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getP_number() {
		return p_number;
	}
	public void setP_number(String p_number) {
		this.p_number = p_number;
	}
	public String getP_phone() {
		return p_phone;
	}
	public void setP_phone(String p_phone) {
		this.p_phone = p_phone;
	}
	public String getHelper() {
		return helper;
	}
	public void setHelper(String helper) {
		this.helper = helper;
	}
	public String getH_number() {
		return h_number;
	}
	public void setH_number(String h_number) {
		this.h_number = h_number;
	}
	public String getH_phone() {
		return h_phone;
	}
	public void setH_phone(String h_phone) {
		this.h_phone = h_phone;
	}
	public String getUser_loc() {
		return user_loc;
	}
	public void setUser_loc(String user_loc) {
		this.user_loc = user_loc;
	}
	public String getContent() {
		if(content==null)
		{
			return "空";
		}
		else return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getInfor()
	{
		if(infor==null)
		{
			return "空";
		}
		else return infor;
	}
	public void setInfor(String infor) {
		this.infor = infor;
	}
	public String getR_nameORmessage() {

		if(r_nameORmessage==null)
		{
			return "空";
		}
		else return r_nameORmessage;
	}
	public void setR_nameORmessage(String r_nameORmessage) {
		this.r_nameORmessage = r_nameORmessage;
	}
	public String getR_locORpackage_loc() {

		if(r_locORpackage_loc==null)
		{
			return "空";
		}
		else return r_locORpackage_loc;

	}
	public void setR_locORpackage_loc(String r_locORpackage_loc) {
		this.r_locORpackage_loc = r_locORpackage_loc;
	}
	public String getR_phoneORphone() {

		if(r_phoneORphone==null)
		{
			return "空";
		}
		else return r_phoneORphone;

	}
	public void setR_phoneORphone(String r_phoneORphone) {
		this.r_phoneORphone = r_phoneORphone;
	}
	public String getNullORpackage_Id() {
		if(nullORpackage_Id==null)
		{
			return "空";
		}
		else return nullORpackage_Id;

	}
	public void setNullORpackage_Id(String nullORpackage_Id) {
		this.nullORpackage_Id = nullORpackage_Id;
	}

}
