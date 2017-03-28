package ru.vyukov.anotherliverefresh.interceptor;

import javax.servlet.http.HttpServletResponse;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Interceptor of "render" method in View. After execution adds "refresh code"
 * 
 * @author gelo
 *
 */
public class RenderMethodInterceptor implements MethodInterceptor {

	private static final String REFRESH_CODE = "<script src=\"/alr/refresh.js\"></script>";

	/**
	 * Name for {@link org.springframework.web.servlet.View#render}
	 */
	public final static String METHOD_NAME = "render";

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object[] args = invocation.getArguments();

		// org.springframework.web.servlet.View#render
		HttpServletResponse originalResponse = (HttpServletResponse) args[2];
		
		// wrap response
		HtmlResponseWrapper response = new HtmlResponseWrapper((HttpServletResponse) originalResponse);
		args[2] = response;
		Object result = invocation.proceed();

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

		return result;
	}
}
