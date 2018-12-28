package com.bishe.portal.web.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.util.StreamUtils;


/**
 * @author 熊猫
 */
public class  HttpMessageConverter extends AbstractHttpMessageConverter<String> {
	
	private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
	private final Charset defaultCharset;
	private final List<Charset> availableCharsets;
	private boolean writeAcceptCharset;

	public HttpMessageConverter() {
		this(DEFAULT_CHARSET);
	}

	public HttpMessageConverter(Charset defaultCharset) {
		super(new MediaType[] { new MediaType("text", "plain", defaultCharset),MediaType.ALL });
		this.writeAcceptCharset = true;
		this.defaultCharset = defaultCharset;
		this.availableCharsets = new ArrayList(Charset.availableCharsets().values());
	}

	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return String.class.equals(clazz);
	}

	@Override
	protected String readInternal(Class<? extends String> clazz, HttpInputMessage inputMessage) throws IOException {
		Charset charset = getContentTypeCharset(inputMessage.getHeaders().getContentType());
		return StreamUtils.copyToString(inputMessage.getBody(), charset);
	}

	@Override
	protected Long getContentLength(String s, MediaType contentType) {
		Charset charset = getContentTypeCharset(contentType);
		try {
			return (long) s.getBytes(charset.name()).length;
		} catch (UnsupportedEncodingException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	protected void writeInternal(String s, HttpOutputMessage outputMessage)throws IOException {
		if (this.writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(getAcceptedCharsets());
		}
		Charset charset = getContentTypeCharset(outputMessage.getHeaders().getContentType());
		StreamUtils.copy(s, charset, outputMessage.getBody());
	}

	private List<Charset> getAcceptedCharsets() {
		return this.availableCharsets;
	}

	private Charset getContentTypeCharset(MediaType contentType) {
		if ((contentType != null) && (contentType.getCharSet() != null)) {
			return contentType.getCharSet();
		}
		return this.defaultCharset;
	}

}
