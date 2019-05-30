import java.io.*;

public class ReadFile {
	public static StringBuffer Read() {
		int b = 0;
		StringBuffer buffer = new StringBuffer();
		FileInputStream file = null;
		try {
			file = new FileInputStream("examples/Stack.h");
			b = file.read();
			while(b != -1) {
				buffer.append((char)b);
				b = file.read();
			}
			
			return buffer;
		} catch (FileNotFoundException e) {
			System.out.println("Oops : FileNOtFoundException");
		} catch (IOException e) {
			System.out.println("Input error");
		}
		return null;
	}
}
