package com.jtt.hhl.testapi.interceptor;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;

/**
 * java类作用描述
 *
 * @author Herman
 * @createDate 2020/5/7 20:21
 */
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public MyHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        String sessionStream = getBodyString(request);
        body = sessionStream.getBytes(Charset.forName("UTF-8"));
    }

    /**
     * 获取请求Body
     *
     * @author      Herman
     * @createDate  2020/5/7 20:24
     * @param request :
     * @return      java.lang.String
     */
    public String getBodyString(final ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try ( InputStream inputStream = cloneInputStream(request.getInputStream());
              BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))){
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }


    /**
     * 复制输入流
     *
     * @author      Herman
     * @createDate  2020/5/7 20:25
     * @param inputStream :
     * @return      java.io.InputStream
     */
    public InputStream cloneInputStream(ServletInputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            byteArrayOutputStream.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        InputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return byteArrayInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

}
