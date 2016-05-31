package com.ionesmile.variousdemo.utils;

public class AlarmBean {

	private int id;
	private String name;
	private Day[] days;
	
	
	public enum Day{
		SUNDAY,
		MONDAY,
		TUESDAY,
		WEDNESDAY,
		THURSDAY,
		FRIDAY,
		SATURDAY;
	}
}
