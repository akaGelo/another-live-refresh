package ru.vyukov.anotherliverefresh.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class LiveRefreshIncludeFilter implements Filter {

	private static final String REFRESH_CODE = "<script src=\"/alr/refresh.js\"></script>";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HtmlResponseWrapper capturingResponseWrapper = new HtmlResponseWrapper((HttpServletResponse) response);

		chain.doFilter(request, capturingResponseWrapper);

		String content = capturingResponseWrapper.getAsString();
		if (response.getContentType() != null && response.getContentType().contains("text/html")) {
			content = content.replace("</head>", REFRESH_CODE + "\n</head>");
		}

		response.setContentLength(content.getBytes().length);
		response.getWriter().write(content);

	}

	@Override
	public void destroy() {

	}

}
