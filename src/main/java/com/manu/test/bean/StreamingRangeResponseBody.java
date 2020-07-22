package com.manu.test.bean;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public class StreamingRangeResponseBody implements StreamingResponseBody {
	
	public static final Logger logger = LoggerFactory.getLogger(StreamingRangeResponseBody.class);
	
	private int length;
    private RandomAccessFile raf;
    final byte[] buf = new byte[4096];

    public StreamingRangeResponseBody( int length, RandomAccessFile raf ) {
        this.length = length;
        this.raf = raf;
    }
	
	@Override
	public void writeTo(OutputStream outputStream) throws IOException {
		try {
            while( length != 0) {
                int read = raf.read( buf, 0, buf.length > length ? length : buf.length );
                outputStream.write( buf, 0, read );
                length -= read;
            }
        } 
        finally {
            raf.close();
        }
	}
	
	public int getLength() {
        return length;
    }

}
