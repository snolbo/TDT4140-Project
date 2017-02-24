package server;

import java.util.Random;

public class ThreadExample implements Runnable{
	String name;
	int time;
	Random r = new Random();
	
	public ThreadExample(String name){
		this.name = name;
		this.time = r.nextInt(1000);
	}
	
	// This runs whenever a Thread is made
	public void run(){
		try{
			System.out.printf("%s is sleeping for %d\n", name, time);
			Thread.sleep(time);
			System.out.printf("%s is done \n", name);
		}catch (Exception e){
			
		}
	}
	
	
}