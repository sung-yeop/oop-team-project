import java.io.*;

public class Parsing {
	public void Parse() {
		int b = 0;
		StringBuffer buffer = new StringBuffer();
		FileInputStream file = null;
		try {
			file =new FileInputStream("examples/Stack.h");
			b = file.read();
			while(b != -1) {
				buffer.append((char)b);
				b = file.read();
			}
			System.out.println(buffer);
		} catch (FileNotFoundException e) {
			System.out.println("Oops : FileNOtFoundException");
		} catch (IOException e) {
			System.out.println("Input error");
		}
	}
}
