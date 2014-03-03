package Christ;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static int[] time;
	public static int[] nextState;
	public static int pedestrian_time = 0;
	
	public static void main(String args[]) {
		
		String stringinput = new String();
		String stringinput2 = new String();
		try {
			BufferedReader br = new BufferedReader(new FileReader("in.txt"));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			stringinput = sb.toString();
			br.close();
			
			//baca file external ke-2
			BufferedReader br2 = new BufferedReader(new FileReader("in2.txt"));
			StringBuilder sb2 = new StringBuilder();
			String line2 = br2.readLine();
			
			while (line2 != null) {
				sb2.append(line2);
				line2 = br2.readLine();
			}
			stringinput2 = sb2.toString();
			br2.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		char[] input = new char[stringinput.length()];
		char[] input2 = new char[stringinput2.length()];
		input = stringinput.toCharArray();
		input2 = stringinput2.toCharArray();

		Main.time = new int[8];
		Main.nextState = new int[8];

		int n_time = 0, n_value = 0;
		for (int i=0; i<input.length; ++i) {
			if (input[i] == 'q') {
				i++;
				n_time = input[i] - '0';
				i++;
			} else if (input[i] == ' ') {
				Main.time[n_time] = n_value;
				n_value = 0;
				i += 2;
				nextState[n_time] = input[i] - '0';
				i += 2; //kenapa +4? harusnya +3
			} else {
				n_value *= 10;
				n_value += input[i] - '0';
			}
		}
		
		
		n_value = 0;
		for (int i=0; i<input2.length; ++i) {
			if (input2[i] == '.') {
				pedestrian_time = n_value;
			} else {
				n_value *= 10;
				n_value += input2[i] - '0';
			}
		}
		
		for (int i=0; i<Main.time.length; ++i) {
			System.out.println("Time " + Main.time[i]);
			System.out.println("nextState " + Main.nextState[i]);
		}
	}
	

}
