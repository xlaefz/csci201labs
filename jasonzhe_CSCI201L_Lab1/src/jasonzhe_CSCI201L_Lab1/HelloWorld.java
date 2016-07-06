package jasonzhe_CSCI201L_Lab1;

import java.util.Scanner;

public class HelloWorld{
	public static void main(String [] args){
		Scanner in = new Scanner(System.in);
		System.out.print("Enter your name: ");
		String name = in.nextLine();
		in.close();
		System.out.println("Hello " + name);
	}
	
}