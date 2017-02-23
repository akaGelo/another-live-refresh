package ru.vyukov.anotherliverefresh.filter;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class ByteArrayServletOutputStream extends ServletOutputStream {
	private OutputStream capture;

	public ByteArrayServletOutputStream(OutputStream capture) {
		super();
		this.capture = capture;
	}

	@Override
	public void write(int b) throws IOException {
		capture.write(b);
	}

	@Override
	public void flush() throws IOException {
		capture.flush();
	}

	@Override
	public void close() throws IOException {
		capture.close();
	}

	@Override
	public boolean isReady() {
		return false;
	}

	@Override
	public void setWriteListener(WriteListener arg0) {
	}
}