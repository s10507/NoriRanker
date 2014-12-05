package norites;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

public class TestGZIP {
	void main(){
	
		InputStream in = new ByteArrayInputStream("H4sIAAAAAAAAC2NgGAXDGTCRiXHpBQCie5DKMAIAAA==".getBytes(StandardCharsets.UTF_8));
		try{
			GZIPInputStream a = new GZIPInputStream(in);
	
		}catch(IOException e){
		}
	}
	
}
