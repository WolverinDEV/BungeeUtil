package dev.wolveringer.util.apache;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.SequenceInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class IOUtils {
	@SuppressWarnings("unused")
	private static final int EOF = -1;
	public static final char DIR_SEPARATOR_UNIX = '/';
	public static final char DIR_SEPARATOR_WINDOWS = '\\';
	public static final char DIR_SEPARATOR = File.separatorChar;
	public static final String LINE_SEPARATOR_UNIX = "\n";
	public static final String LINE_SEPARATOR_WINDOWS = "\r\n";
	public static final String LINE_SEPARATOR;
	@SuppressWarnings("unused")
	private static final int DEFAULT_BUFFER_SIZE = 4096;
	@SuppressWarnings("unused")
	private static final int SKIP_BUFFER_SIZE = 2048;
	private static char[] SKIP_CHAR_BUFFER;
	private static byte[] SKIP_BYTE_BUFFER;

	static{
		StringBuilderWriter buf = new StringBuilderWriter(4);
		PrintWriter out = new PrintWriter(buf);
		out.println();
		LINE_SEPARATOR = buf.toString();
		out.close();
	}

	public static void close(URLConnection conn) {
		if((conn instanceof HttpURLConnection)){
			((HttpURLConnection) conn).disconnect();
		}
	}

	public static void closeQuietly(Reader input) {
		closeQuietly((Closeable) input);
	}

	public static void closeQuietly(Writer output) {
		closeQuietly((Closeable) output);
	}

	public static void closeQuietly(InputStream input) {
		closeQuietly((Closeable) input);
	}

	public static void closeQuietly(OutputStream output) {
		closeQuietly((Closeable) output);
	}

	public static void closeQuietly(Closeable closeable) {
		try{
			if(closeable != null){
				closeable.close();
			}
		}catch (IOException ioe){
		}
	}

	public static void closeQuietly(Socket sock) {
		if(sock != null){
			try{
				sock.close();
			}catch (IOException ioe){
			}
		}
	}

	public static void closeQuietly(Selector selector) {
		if(selector != null){
			try{
				selector.close();
			}catch (IOException ioe){
			}
		}
	}

	public static void closeQuietly(ServerSocket sock) {
		if(sock != null){
			try{
				sock.close();
			}catch (IOException ioe){
			}
		}
	}

	public static InputStream toBufferedInputStream(InputStream input) throws IOException {
		return ByteArrayOutputStream.toBufferedInputStream(input);
	}

	public static BufferedReader toBufferedReader(Reader reader) {
		return (reader instanceof BufferedReader) ? (BufferedReader) reader : new BufferedReader(reader);
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static byte[] toByteArray(InputStream input, long size) throws IOException {
		if(size > 2147483647L){
			throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
		}
		return toByteArray(input, (int) size);
	}

	public static byte[] toByteArray(InputStream input, int size) throws IOException {
		if(size < 0){
			throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
		}
		if(size == 0){
			return new byte[0];
		}
		byte[] data = new byte[size];
		int offset = 0;
		int readed;
		while ((offset < size) && ((readed = input.read(data, offset, size - offset)) != -1)){
			offset += readed;
		}
		if(offset != size){
			throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
		}
		return data;
	}

	public static byte[] toByteArray(Reader input) throws IOException {
		return toByteArray(input, Charset.defaultCharset());
	}

	public static byte[] toByteArray(Reader input, Charset encoding) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output, encoding);
		return output.toByteArray();
	}

	public static byte[] toByteArray(Reader input, String encoding) throws IOException {
		return toByteArray(input, Charsets.toCharset(encoding));
	}

	@Deprecated
	public static byte[] toByteArray(String input) throws IOException {
		return input.getBytes();
	}

	public static byte[] toByteArray(URI uri) throws IOException {
		return toByteArray(uri.toURL());
	}

	public static byte[] toByteArray(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		try{
			return toByteArray(conn);
		}finally{
			close(conn);
		}
	}

	public static byte[] toByteArray(URLConnection urlConn) throws IOException {
		InputStream inputStream = urlConn.getInputStream();
		try{
			return toByteArray(inputStream);
		}finally{
			inputStream.close();
		}
	}

	public static char[] toCharArray(InputStream is) throws IOException {
		return toCharArray(is, Charset.defaultCharset());
	}

	public static char[] toCharArray(InputStream is, Charset encoding) throws IOException {
		CharArrayWriter output = new CharArrayWriter();
		copy(is, output, encoding);
		return output.toCharArray();
	}

	public static char[] toCharArray(InputStream is, String encoding) throws IOException {
		return toCharArray(is, Charsets.toCharset(encoding));
	}

	public static char[] toCharArray(Reader input) throws IOException {
		CharArrayWriter sw = new CharArrayWriter();
		copy(input, sw);
		return sw.toCharArray();
	}

	public static String toString(InputStream input) throws IOException {
		return toString(input, Charset.defaultCharset());
	}

	public static String toString(InputStream input, Charset encoding) throws IOException {
		StringBuilderWriter sw = new StringBuilderWriter();
		copy(input, sw, encoding);
		return sw.toString();
	}

	public static String toString(InputStream input, String encoding) throws IOException {
		return toString(input, Charsets.toCharset(encoding));
	}

	public static String toString(Reader input) throws IOException {
		StringBuilderWriter sw = new StringBuilderWriter();
		copy(input, sw);
		return sw.toString();
	}

	public static String toString(URI uri) throws IOException {
		return toString(uri, Charset.defaultCharset());
	}

	public static String toString(URI uri, Charset encoding) throws IOException {
		return toString(uri.toURL(), Charsets.toCharset(encoding));
	}

	public static String toString(URI uri, String encoding) throws IOException {
		return toString(uri, Charsets.toCharset(encoding));
	}

	public static String toString(URL url) throws IOException {
		return toString(url, Charset.defaultCharset());
	}

	public static String toString(URL url, Charset encoding) throws IOException {
		InputStream inputStream = url.openStream();
		try{
			return toString(inputStream, encoding);
		}finally{
			inputStream.close();
		}
	}

	public static String toString(URL url, String encoding) throws IOException {
		return toString(url, Charsets.toCharset(encoding));
	}

	@Deprecated
	public static String toString(byte[] input) throws IOException {
		return new String(input);
	}

	public static String toString(byte[] input, String encoding) throws IOException {
		return new String(input, Charsets.toCharset(encoding));
	}

	public static List<String> readLines(InputStream input) throws IOException {
		return readLines(input, Charset.defaultCharset());
	}

	public static List<String> readLines(InputStream input, Charset encoding) throws IOException {
		InputStreamReader reader = new InputStreamReader(input, Charsets.toCharset(encoding));
		return readLines(reader);
	}

	public static List<String> readLines(InputStream input, String encoding) throws IOException {
		return readLines(input, Charsets.toCharset(encoding));
	}

	public static List<String> readLines(Reader input) throws IOException {
		BufferedReader reader = toBufferedReader(input);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<String> list = new ArrayList();
		String line = reader.readLine();
		while (line != null){
			list.add(line);
			line = reader.readLine();
		}
		return list;
	}

	public static LineIterator lineIterator(Reader reader) {
		return new LineIterator(reader);
	}

	public static LineIterator lineIterator(InputStream input, Charset encoding) throws IOException {
		return new LineIterator(new InputStreamReader(input, Charsets.toCharset(encoding)));
	}

	public static LineIterator lineIterator(InputStream input, String encoding) throws IOException {
		return lineIterator(input, Charsets.toCharset(encoding));
	}

	public static InputStream toInputStream(CharSequence input) {
		return toInputStream(input, Charset.defaultCharset());
	}

	public static InputStream toInputStream(CharSequence input, Charset encoding) {
		return toInputStream(input.toString(), encoding);
	}

	public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
		return toInputStream(input, Charsets.toCharset(encoding));
	}

	public static InputStream toInputStream(String input) {
		return toInputStream(input, Charset.defaultCharset());
	}

	public static InputStream toInputStream(String input, Charset encoding) {
		return new ByteArrayInputStream(input.getBytes(Charsets.toCharset(encoding)));
	}

	public static InputStream toInputStream(String input, String encoding) throws IOException {
		byte[] bytes = input.getBytes(Charsets.toCharset(encoding));
		return new ByteArrayInputStream(bytes);
	}

	public static void write(byte[] data, OutputStream output) throws IOException {
		if(data != null){
			output.write(data);
		}
	}

	public static void write(byte[] data, Writer output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	public static void write(byte[] data, Writer output, Charset encoding) throws IOException {
		if(data != null){
			output.write(new String(data, Charsets.toCharset(encoding)));
		}
	}

	public static void write(byte[] data, Writer output, String encoding) throws IOException {
		write(data, output, Charsets.toCharset(encoding));
	}

	public static void write(char[] data, Writer output) throws IOException {
		if(data != null){
			output.write(data);
		}
	}

	public static void write(char[] data, OutputStream output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	public static void write(char[] data, OutputStream output, Charset encoding) throws IOException {
		if(data != null){
			output.write(new String(data).getBytes(Charsets.toCharset(encoding)));
		}
	}

	public static void write(char[] data, OutputStream output, String encoding) throws IOException {
		write(data, output, Charsets.toCharset(encoding));
	}

	public static void write(CharSequence data, Writer output) throws IOException {
		if(data != null){
			write(data.toString(), output);
		}
	}

	public static void write(CharSequence data, OutputStream output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
		if(data != null){
			write(data.toString(), output, encoding);
		}
	}

	public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
		write(data, output, Charsets.toCharset(encoding));
	}

	public static void write(String data, Writer output) throws IOException {
		if(data != null){
			output.write(data);
		}
	}

	public static void write(String data, OutputStream output) throws IOException {
		write(data, output, Charset.defaultCharset());
	}

	public static void write(String data, OutputStream output, Charset encoding) throws IOException {
		if(data != null){
			output.write(data.getBytes(Charsets.toCharset(encoding)));
		}
	}

	public static void write(String data, OutputStream output, String encoding) throws IOException {
		write(data, output, Charsets.toCharset(encoding));
	}

	@Deprecated
	public static void write(StringBuffer data, Writer output) throws IOException {
		if(data != null){
			output.write(data.toString());
		}
	}

	@Deprecated
	public static void write(StringBuffer data, OutputStream output) throws IOException {
		write(data, output, (String) null);
	}

	@Deprecated
	public static void write(StringBuffer data, OutputStream output, String encoding) throws IOException {
		if(data != null){
			output.write(data.toString().getBytes(Charsets.toCharset(encoding)));
		}
	}

	public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output) throws IOException {
		writeLines(lines, lineEnding, output, Charset.defaultCharset());
	}

	public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset encoding) throws IOException {
		if(lines == null){
			return;
		}
		if(lineEnding == null){
			lineEnding = LINE_SEPARATOR;
		}
		Charset cs = Charsets.toCharset(encoding);
		for(Object line : lines){
			if(line != null){
				output.write(line.toString().getBytes(cs));
			}
			output.write(lineEnding.getBytes(cs));
		}
	}

	public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, String encoding) throws IOException {
		writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
	}

	public static void writeLines(Collection<?> lines, String lineEnding, Writer writer) throws IOException {
		if(lines == null){
			return;
		}
		if(lineEnding == null){
			lineEnding = LINE_SEPARATOR;
		}
		for(Object line : lines){
			if(line != null){
				writer.write(line.toString());
			}
			writer.write(lineEnding);
		}
	}

	public static int copy(InputStream input, OutputStream output) throws IOException {
		long count = copyLarge(input, output);
		if(count > 2147483647L){
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(InputStream input, OutputStream output) throws IOException {
		return copyLarge(input, output, new byte[4096]);
	}

	public static long copyLarge(InputStream input, OutputStream output, byte[] buffer) throws IOException {
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))){
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length) throws IOException {
		return copyLarge(input, output, inputOffset, length, new byte[4096]);
	}

	public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer) throws IOException {
		if(inputOffset > 0L){
			skipFully(input, inputOffset);
		}
		if(length == 0L){
			return 0L;
		}
		int bufferLength = buffer.length;
		int bytesToRead = bufferLength;
		if((length > 0L) && (length < bufferLength)){
			bytesToRead = (int) length;
		}
		long totalRead = 0L;
		int read;
		while ((bytesToRead > 0) && (-1 != (read = input.read(buffer, 0, bytesToRead)))){
			output.write(buffer, 0, read);
			totalRead += read;
			if(length > 0L){
				bytesToRead = (int) Math.min(length - totalRead, bufferLength);
			}
		}
		return totalRead;
	}

	public static void copy(InputStream input, Writer output) throws IOException {
		copy(input, output, Charset.defaultCharset());
	}

	public static void copy(InputStream input, Writer output, Charset encoding) throws IOException {
		InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(encoding));
		copy(in, output);
	}

	public static void copy(InputStream input, Writer output, String encoding) throws IOException {
		copy(input, output, Charsets.toCharset(encoding));
	}

	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		if(count > 2147483647L){
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(Reader input, Writer output) throws IOException {
		return copyLarge(input, output, new char[4096]);
	}

	public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))){
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

	public static long copyLarge(Reader input, Writer output, long inputOffset, long length) throws IOException {
		return copyLarge(input, output, inputOffset, length, new char[4096]);
	}

	public static long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer) throws IOException {
		if(inputOffset > 0L){
			skipFully(input, inputOffset);
		}
		if(length == 0L){
			return 0L;
		}
		int bytesToRead = buffer.length;
		if((length > 0L) && (length < buffer.length)){
			bytesToRead = (int) length;
		}
		long totalRead = 0L;
		int read;
		while ((bytesToRead > 0) && (-1 != (read = input.read(buffer, 0, bytesToRead)))){
			output.write(buffer, 0, read);
			totalRead += read;
			if(length > 0L){
				bytesToRead = (int) Math.min(length - totalRead, buffer.length);
			}
		}
		return totalRead;
	}

	public static void copy(Reader input, OutputStream output) throws IOException {
		copy(input, output, Charset.defaultCharset());
	}

	public static void copy(Reader input, OutputStream output, Charset encoding) throws IOException {
		OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(encoding));
		copy(input, out);

		out.flush();
	}

	public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
		copy(input, output, Charsets.toCharset(encoding));
	}

	public static boolean contentEquals(InputStream input1, InputStream input2) throws IOException {
		if(!(input1 instanceof BufferedInputStream)){
			input1 = new BufferedInputStream(input1);
		}
		if(!(input2 instanceof BufferedInputStream)){
			input2 = new BufferedInputStream(input2);
		}
		int ch = input1.read();
		while (-1 != ch){
			int ch2 = input2.read();
			if(ch != ch2){
				return false;
			}
			ch = input1.read();
		}
		int ch2 = input2.read();
		return ch2 == -1;
	}

	public static boolean contentEquals(Reader input1, Reader input2) throws IOException {
		input1 = toBufferedReader(input1);
		input2 = toBufferedReader(input2);

		int ch = input1.read();
		while (-1 != ch){
			int ch2 = input2.read();
			if(ch != ch2){
				return false;
			}
			ch = input1.read();
		}
		int ch2 = input2.read();
		return ch2 == -1;
	}

	public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2) throws IOException {
		BufferedReader br1 = toBufferedReader(input1);
		BufferedReader br2 = toBufferedReader(input2);

		String line1 = br1.readLine();
		String line2 = br2.readLine();
		while ((line1 != null) && (line2 != null) && (line1.equals(line2))){
			line1 = br1.readLine();
			line2 = br2.readLine();
		}
		return line1 == null ? false : line2 == null ? true : line1.equals(line2);
	}

	public static long skip(InputStream input, long toSkip) throws IOException {
		if(toSkip < 0L){
			throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
		}
		if(SKIP_BYTE_BUFFER == null){
			SKIP_BYTE_BUFFER = new byte[2048];
		}
		long remain = toSkip;
		while (remain > 0L){
			long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, 2048L));
			if(n < 0L){
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	public static long skip(Reader input, long toSkip) throws IOException {
		if(toSkip < 0L){
			throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
		}
		if(SKIP_CHAR_BUFFER == null){
			SKIP_CHAR_BUFFER = new char[2048];
		}
		long remain = toSkip;
		while (remain > 0L){
			long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, 2048L));
			if(n < 0L){
				break;
			}
			remain -= n;
		}
		return toSkip - remain;
	}

	public static void skipFully(InputStream input, long toSkip) throws IOException {
		if(toSkip < 0L){
			throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
		}
		long skipped = skip(input, toSkip);
		if(skipped != toSkip){
			throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
		}
	}

	public static void skipFully(Reader input, long toSkip) throws IOException {
		long skipped = skip(input, toSkip);
		if(skipped != toSkip){
			throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
		}
	}

	public static int read(Reader input, char[] buffer, int offset, int length) throws IOException {
		if(length < 0){
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0){
			int location = length - remaining;
			int count = input.read(buffer, offset + location, remaining);
			if(-1 == count){
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	public static int read(Reader input, char[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	public static int read(InputStream input, byte[] buffer, int offset, int length) throws IOException {
		if(length < 0){
			throw new IllegalArgumentException("Length must not be negative: " + length);
		}
		int remaining = length;
		while (remaining > 0){
			int location = length - remaining;
			int count = input.read(buffer, offset + location, remaining);
			if(-1 == count){
				break;
			}
			remaining -= count;
		}
		return length - remaining;
	}

	public static int read(InputStream input, byte[] buffer) throws IOException {
		return read(input, buffer, 0, buffer.length);
	}

	public static void readFully(Reader input, char[] buffer, int offset, int length) throws IOException {
		int actual = read(input, buffer, offset, length);
		if(actual != length){
			throw new EOFException("Length to read: " + length + " actual: " + actual);
		}
	}

	public static void readFully(Reader input, char[] buffer) throws IOException {
		readFully(input, buffer, 0, buffer.length);
	}

	public static void readFully(InputStream input, byte[] buffer, int offset, int length) throws IOException {
		int actual = read(input, buffer, offset, length);
		if(actual != length){
			throw new EOFException("Length to read: " + length + " actual: " + actual);
		}
	}

	public static void readFully(InputStream input, byte[] buffer) throws IOException {
		readFully(input, buffer, 0, buffer.length);
	}
}

@SuppressWarnings("serial")
class StringBuilderWriter extends Writer implements Serializable {
	private final StringBuilder builder;

	public StringBuilderWriter() {
		this.builder = new StringBuilder();
	}

	public StringBuilderWriter(int capacity) {
		this.builder = new StringBuilder(capacity);
	}

	public StringBuilderWriter(StringBuilder builder) {
		this.builder = (builder != null ? builder : new StringBuilder());
	}

	public Writer append(char value) {
		this.builder.append(value);
		return this;
	}

	public Writer append(CharSequence value) {
		this.builder.append(value);
		return this;
	}

	public Writer append(CharSequence value, int start, int end) {
		this.builder.append(value, start, end);
		return this;
	}

	public void close() {
	}

	public void flush() {
	}

	public void write(String value) {
		if(value != null){
			this.builder.append(value);
		}
	}

	public void write(char[] value, int offset, int length) {
		if(value != null){
			this.builder.append(value, offset, length);
		}
	}

	public StringBuilder getBuilder() {
		return this.builder;
	}

	public String toString() {
		return this.builder.toString();
	}
}

class ByteArrayOutputStream extends OutputStream {
	private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private final List<byte[]> buffers = new ArrayList();
	private int currentBufferIndex;
	private int filledBufferSum;
	private byte[] currentBuffer;
	private int count;

	public ByteArrayOutputStream() {
		this(1024);
	}

	public ByteArrayOutputStream(int size) {
		if(size < 0){
			throw new IllegalArgumentException("Negative initial size: " + size);
		}
		synchronized (this){
			needNewBuffer(size);
		}
	}

	private void needNewBuffer(int newcount) {
		if(this.currentBufferIndex < this.buffers.size() - 1){
			this.filledBufferSum += this.currentBuffer.length;

			this.currentBufferIndex += 1;
			this.currentBuffer = ((byte[]) this.buffers.get(this.currentBufferIndex));
		}else{
			int newBufferSize;
			if(this.currentBuffer == null){
				newBufferSize = newcount;
				this.filledBufferSum = 0;
			}else{
				newBufferSize = Math.max(this.currentBuffer.length << 1, newcount - this.filledBufferSum);

				this.filledBufferSum += this.currentBuffer.length;
			}
			this.currentBufferIndex += 1;
			this.currentBuffer = new byte[newBufferSize];
			this.buffers.add(this.currentBuffer);
		}
	}

	public void write(byte[] b, int off, int len) {
		if((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0)){
			throw new IndexOutOfBoundsException();
		}
		if(len == 0){
			return;
		}
		synchronized (this){
			int newcount = this.count + len;
			int remaining = len;
			int inBufferPos = this.count - this.filledBufferSum;
			while (remaining > 0){
				int part = Math.min(remaining, this.currentBuffer.length - inBufferPos);
				System.arraycopy(b, off + len - remaining, this.currentBuffer, inBufferPos, part);
				remaining -= part;
				if(remaining > 0){
					needNewBuffer(newcount);
					inBufferPos = 0;
				}
			}
			this.count = newcount;
		}
	}

	public synchronized void write(int b) {
		int inBufferPos = this.count - this.filledBufferSum;
		if(inBufferPos == this.currentBuffer.length){
			needNewBuffer(this.count + 1);
			inBufferPos = 0;
		}
		this.currentBuffer[inBufferPos] = ((byte) b);
		this.count += 1;
	}

	public synchronized int write(InputStream in) throws IOException {
		int readCount = 0;
		int inBufferPos = this.count - this.filledBufferSum;
		int n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
		while (n != -1){
			readCount += n;
			inBufferPos += n;
			this.count += n;
			if(inBufferPos == this.currentBuffer.length){
				needNewBuffer(this.currentBuffer.length);
				inBufferPos = 0;
			}
			n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
		}
		return readCount;
	}

	public synchronized int size() {
		return this.count;
	}

	public void close() throws IOException {
	}

	public synchronized void reset() {
		this.count = 0;
		this.filledBufferSum = 0;
		this.currentBufferIndex = 0;
		this.currentBuffer = ((byte[]) this.buffers.get(this.currentBufferIndex));
	}

	public synchronized void writeTo(OutputStream out) throws IOException {
		int remaining = this.count;
		for(byte[] buf : this.buffers){
			int c = Math.min(buf.length, remaining);
			out.write(buf, 0, c);
			remaining -= c;
			if(remaining == 0){
				break;
			}
		}
	}

	@SuppressWarnings("resource")
	public static InputStream toBufferedInputStream(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		output.write(input);
		return output.toBufferedInputStream();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private InputStream toBufferedInputStream() {
		int remaining = this.count;
		if(remaining == 0){
			return new ClosedInputStream();
		}
		List<ByteArrayInputStream> list = new ArrayList(this.buffers.size());
		for(byte[] buf : this.buffers){
			int c = Math.min(buf.length, remaining);
			list.add(new ByteArrayInputStream(buf, 0, c));
			remaining -= c;
			if(remaining == 0){
				break;
			}
		}
		return new SequenceInputStream(Collections.enumeration(list));
	}

	public synchronized byte[] toByteArray() {
		int remaining = this.count;
		if(remaining == 0){
			return EMPTY_BYTE_ARRAY;
		}
		byte[] newbuf = new byte[remaining];
		int pos = 0;
		for(byte[] buf : this.buffers){
			int c = Math.min(buf.length, remaining);
			System.arraycopy(buf, 0, newbuf, pos, c);
			pos += c;
			remaining -= c;
			if(remaining == 0){
				break;
			}
		}
		return newbuf;
	}

	public String toString() {
		return new String(toByteArray());
	}

	public String toString(String enc) throws UnsupportedEncodingException {
		return new String(toByteArray(), enc);
	}
}

class ClosedInputStream extends InputStream {
	public static final ClosedInputStream CLOSED_INPUT_STREAM = new ClosedInputStream();

	public int read() {
		return -1;
	}
}

class Charsets {
	public static Charset toCharset(Charset charset) {
		return charset == null ? Charset.defaultCharset() : charset;
	}

	public static Charset toCharset(String charset) {
		return charset == null ? Charset.defaultCharset() : Charset.forName(charset);
	}

	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
	public static final Charset US_ASCII = Charset.forName("US-ASCII");
	public static final Charset UTF_16 = Charset.forName("UTF-16");
	public static final Charset UTF_16BE = Charset.forName("UTF-16BE");
	public static final Charset UTF_16LE = Charset.forName("UTF-16LE");
	public static final Charset UTF_8 = Charset.forName("UTF-8");
}

/**
 * An Iterator over the lines in a <code>Reader.
 * <p>
 * <code>LineIterator holds a reference to an open Reader.
 * When you have finished with the iterator you should close the reader
 * to free internal resources. This can be done by closing the reader directly,
 * or by calling the {@link #close()} or {@link #closeQuietly(LineIterator)}
 * method on the iterator.
 * <p>
 * The recommended usage pattern is:
 * <pre>
 * LineIterator it = FileUtils.lineIterator(file, "UTF-8");
 * try {
 *   while (it.hasNext()) {
 *     String line = it.nextLine();
 *     // do something with line
 *   }
 * } finally {
 *   it.close();
 * }
 * </pre>
 *
 * @author Niall Pemberton
 * @author Stephen Colebourne
 * @author Sandy McArthur
 * @version $Id: LineIterator.java 1022322 2010-10-13 23:20:42Z ggregory $
 * @since Commons IO 1.2
 */
class LineIterator implements Iterator<String> {

	/** The reader that is being read. */
	private final BufferedReader bufferedReader;
	/** The current line. */
	private String cachedLine;
	/** A flag indicating if the iterator has been fully read. */
	private boolean finished = false;

	/**
	 * Constructs an iterator of the lines for a <code>Reader.
	 *
	 * @param reader
	 *            the <code>Reader to read from, not null
	 * @throws IllegalArgumentException
	 *             if the reader is null
	 */
	public LineIterator(final Reader reader) throws IllegalArgumentException {
		if(reader == null){
			throw new IllegalArgumentException("Reader must not be null");
		}
		if(reader instanceof BufferedReader){
			bufferedReader = (BufferedReader) reader;
		}else{
			bufferedReader = new BufferedReader(reader);
		}
	}

	//-----------------------------------------------------------------------
	/**
	 * Indicates whether the <code>Reader has more lines.
	 * If there is an <code>IOException then {@link #close()} will
	 * be called on this instance.
	 *
	 * @return <code>true if the Reader has more lines
	 * @throws IllegalStateException
	 *             if an IO exception occurs
	 */
	public boolean hasNext() {
		if(cachedLine != null){
			return true;
		}else if(finished){
			return false;
		}else{
			try{
				while (true){
					String line = bufferedReader.readLine();
					if(line == null){
						finished = true;
						return false;
					}else if(isValidLine(line)){
						cachedLine = line;
						return true;
					}
				}
			}catch (IOException ioe){
				close();
				throw new IllegalStateException(ioe);
			}
		}
	}

	/**
	 * Overridable method to validate each line that is returned.
	 *
	 * @param line
	 *            the line that is to be validated
	 * @return true if valid, false to remove from the iterator
	 */
	protected boolean isValidLine(String line) {
		return true;
	}

	/**
	 * Returns the next line in the wrapped <code>Reader.
	 *
	 * @return the next line from the input
	 * @throws NoSuchElementException
	 *             if there is no line to return
	 */
	public String next() {
		return nextLine();
	}

	/**
	 * Returns the next line in the wrapped <code>Reader.
	 *
	 * @return the next line from the input
	 * @throws NoSuchElementException
	 *             if there is no line to return
	 */
	public String nextLine() {
		if(!hasNext()){
			throw new NoSuchElementException("No more lines");
		}
		String currentLine = cachedLine;
		cachedLine = null;
		return currentLine;
	}

	/**
	 * Closes the underlying <code>Reader quietly.
	 * This method is useful if you only want to process the first few
	 * lines of a larger file. If you do not close the iterator
	 * then the <code>Reader remains open.
	 * This method can safely be called multiple times.
	 */
	public void close() {
		finished = true;
		IOUtils.closeQuietly(bufferedReader);
		cachedLine = null;
	}

	/**
	 * Unsupported.
	 *
	 * @throws UnsupportedOperationException
	 *             always
	 */
	public void remove() {
		throw new UnsupportedOperationException("Remove unsupported on LineIterator");
	}

	//-----------------------------------------------------------------------
	/**
	 * Closes the iterator, handling null and ignoring exceptions.
	 *
	 * @param iterator
	 *            the iterator to close
	 */
	public static void closeQuietly(LineIterator iterator) {
		if(iterator != null){
			iterator.close();
		}
	}

}