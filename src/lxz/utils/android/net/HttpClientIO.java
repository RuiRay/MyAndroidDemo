package lxz.utils.android.net;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;

public class HttpClientIO {
	private InputStream inputStream;
	private HttpEntity httpEntity;

	public HttpClientIO() {

	}

	/**
	 * 构造一个可以关闭IO流的对象
	 * 
	 * @param is
	 * @param he
	 * @param hc
	 */
	public HttpClientIO(InputStream is, HttpEntity he) {
		this.inputStream = is;
		this.httpEntity = he;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public void setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
	}

	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	/**
	 * 关闭流
	 */
	public void close() {
		if (this.getInputStream() != null)
			try {
				this.getInputStream().close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (this.getHttpEntity() != null)
			try {
				this.getHttpEntity().consumeContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

}