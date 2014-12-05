package norites;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class TestGZIP {
	public static void main(String[] args) throws IOException{
		String str = "0";
	
		InputStream in = new ByteArrayInputStream("H4sIAAAAAAAAC2NgGAXDGTCRiXHpBQCie5DKMAIAAA==".getBytes(StandardCharsets.UTF_8));
		GZIPInputStream a = new GZIPInputStream(in);
			
		System.out.println(	a.toString());
		 str = a.toString();
	
		
		
		System.out.println(str);
	}
	
}
