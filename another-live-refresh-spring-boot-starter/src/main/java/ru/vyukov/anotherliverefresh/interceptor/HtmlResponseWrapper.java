package ru.vyukov.anotherliverefresh.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class HtmlResponseWrapper extends HttpServletResponseWrapper {

	private ByteArrayOutputStream capture;

	private ServletOutputStream output;

	private PrintWriter writer;

	public HtmlResponseWrapper(HttpServletResponse response) {
		super(response);
		capture = new ByteArrayOutputStream(response.getBufferSize());
	}

	@Override
	public ServletOutputStream getOutputStream() {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called");
		}

		if (output == null) {
			output = new ByteArrayServletOutputStream(capture);
		}

		return output;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (output != null) {
			throw new IllegalStateException("getOutputStream() has already been called");
		}

		if (writer == null) {
			String characterEncoding = getCharacterEncoding();
			writer = new PrintWriter(new OutputStreamWriter(capture, characterEncoding));
		}

		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		super.flushBuffer();

		if (writer != null) {
			writer.flush();
		} else if (output != null) {
			output.flush();
		}
	}

	public byte[] getAsBytes() throws IOException {
		if (writer != null) {
			writer.close();
		} else if (output != null) {
			output.close();
		}

		return capture.toByteArray();
	}

	public String getAsString() throws IOException {
		return new String(getAsBytes(), getCharacterEncoding());
	}

}