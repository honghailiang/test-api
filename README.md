# test-api
api测试demo，不同请求类型的处理application/json及application/x-www-form-urlencoded，针对application/json请求拦截器读取流后，controller层继续使用流的处理


application/x- www-form-urlencoded是Post请求默认的请求体内容类型，也是form表单默认的类型。Servlet API规范中对该类型的请求内容提供了request.getParameter()方法来获取请求参数值。但当请求内容不是该类型时，需要调用request.getInputStream()或request.getReader()方法来获取请求内容值。
当请求体内容（注意：get请求没有请求体） **类型是application/x- www-form-urlencoded时也可以直接调用request.getInputStream()或request.getReader()方法获取到请求内容再解析出具体都参数，但前提是还没调用request.getParameter()方法(先写了getParameter()方法，再用getReader()方法不能取到数据)。此时当request.getInputStream()或request.getReader()获取到请求内容后，无法再调request.getParameter()获取请求内容。即对该类型的请求，三个方法互斥，只能调其中一个。** 今天遇到一个Controller请求经过Spring MVC 的RequestMapping处理后，只能通过request.getParameter()获取到参数、无法通过request.getInputStream()和request.getReader()读取内容很可能就是因为在请求经过Spring MVC时已调用过request.getParameter()方法的原因（第一次都会走获取“_method”的值，源码org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal）。

 protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		HttpServletRequest requestToUse = request;

		if ("POST".equals(request.getMethod()) && request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) == null) {
			String paramValue = request.getParameter(this.methodParam);
			if (StringUtils.hasLength(paramValue)) {
				String method = paramValue.toUpperCase(Locale.ENGLISH);
				if (ALLOWED_METHODS.contains(method)) {
					requestToUse = new HttpMethodRequestWrapper(request, method);
				}
			}
		}

		filterChain.doFilter(requestToUse, response);
	}
注意：在一个请求链中，请求对象被前面对象方法中调用request.getInputStream()或request.getReader()获取过内容后，后面的对象方法里再调用这两个方法也无法获取到客户端请求的内容，但是调用request.getParameter()方法获取过内容后，后面的对象方法里依然可以调用它获取到参数的内容。**（为什么可以再次获取，就是因为放到map中了）**
当请求体内容是其它类型时，比如 multipart/form-data或application/json时，无法通过request.getParameter()获取到请求内容，此时只能通过request.getInputStream()和request.getReader()方法获取请求内容，**此时调用request.getParameter()也不会影响第一次调用request.getInputStream()或request.getReader()获取到请求内容。（原因可以看源码**


org.apache.catalina.connector.Request.parseParameters

         if ("multipart/form-data".equals(contentType)) {
                parseParts(false);
                success = true;
                return;
            }

            if( !getConnector().isParseBodyMethod(getMethod()) ) {
                success = true;
                return;
            }

            if (!("application/x-www-form-urlencoded".equals(contentType))) {   //不是改类型的就不会走读取流的操作
                success = true;
                return;
            }


）直接返回 不走后面的流读取
request.getInputStream()返回请求内容字节流，多用于文件上传，request.getReader()是对前者返回内容的封装，可以让调用者更方便字符内容的处理（不用自己先获取字节流再做字符流的转换操作）。
POST 的三种常用 Content-Type （get没有请求体）
POST 是日常开发中非常常用的数据传输方法，请求头中的 Content-Type 指示了在一次 POST 请求中，服务器端应该如何解析客户端传递过来的数据。对于 web 开发来说， 以下三种 Content-Type 是最为常见的:
application/x-www-form-urlencoded
1.作为 <form /> 元素的默认 Content-Type 设定（enctype 属性），application/x-www-form-urlencoded 可能是被使用得最为广泛的一种数据编码方式。
2.application/json
当作为 domain 接收时，springmvc 无法对 domain 中的复杂数据类型进行解析，在这种情况下，使用 application/json 是更好的选择。application/json 的不同在于，请求体的内容是 JSON 字符串。
3.multipart/form-data
用于文件上传的 Content-Type, 比较特殊的是，对于这样的 Content-Type 设定，浏览器会生成一个比较复杂且随机的boundary，并追加在 Content-Type 之后，用于分割请求体中的文件内容。


localhost:8080/testapi-json?name=honghailiang

content-type=application/json

请求内容
{
"name":"honghailiang",
"address":"github"
}