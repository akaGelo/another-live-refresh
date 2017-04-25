package ru.vyukov.anotherliverefresh.wrap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;

/**
 * DispatcherServlet, which injects in the server's response an refresh.js code.
 * 
 * @author gelo
 *
 */
public class ResponseWrappedDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = 196512000500021250L;

	private static final String REFRESH_CODE = "<script src=\"/alr/refresh.js\"></script>";

	@Override
	protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse originalResponse)
			throws Exception {
		// 1. wrap response
		HtmlResponseWrapper response = new HtmlResponseWrapper((HttpServletResponse) originalResponse);

		// 2. execute default render
		super.render(mv, request, response);

		// 3. write wrapped content to original response
		if (response.getContentType() != null && response.getContentType().contains("text/html")) {

			// modify and write wrapped data
			String content = response.getAsString();
			content = content.replace("</body>", REFRESH_CODE + "\n</body>");

			originalResponse.setContentLength(content.getBytes().length);
			originalResponse.getWriter().write(content);
		} else {
			byte[] content = response.getAsBytes();
			originalResponse.setContentLength(content.length);
			originalResponse.getOutputStream().write(content);
		}
	}
}
