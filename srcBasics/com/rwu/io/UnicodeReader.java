package com.rwu.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;

/**
 * http://stackoverflow.com/questions/1835430/byte-order-mark-screws-up-file-reading-in-java
 *
 */
public class UnicodeReader extends Reader {

	private static final int BOM_SIZE = 4;
	private final InputStreamReader reader;

	/**
	 * Construct UnicodeReader
	 *
	 * @param in Input stream.
	 * @param defaultEncoding Default encoding to be used if BOM is not found,
	 * or <code>null</code> to use system default encoding.
	 * @throws IOException If an I/O error occurs.
	 */
	public UnicodeReader(InputStream in, String defaultEncoding) throws IOException {
		byte bom[] = new byte[BOM_SIZE];
		String encoding;
		int unread;
		PushbackInputStream pushbackStream = new PushbackInputStream(in, BOM_SIZE);
		int n = pushbackStream.read(bom, 0, bom.length);

		// Read ahead four bytes and check for BOM marks.
		if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
			encoding = "UTF-8";
			unread = n - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			encoding = "UTF-16BE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			encoding = "UTF-16LE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			encoding = "UTF-32BE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			encoding = "UTF-32LE";
			unread = n - 4;
		} else {
			encoding = defaultEncoding;
			unread = n;
		}

		// Unread bytes if necessary and skip BOM marks.
		if (unread > 0) {
			pushbackStream.unread(bom, (n - unread), unread);
		} else if (unread < -1) {
			pushbackStream.unread(bom, 0, 0);
		}

		// Use given encoding.
		if (encoding == null) {
			reader = new InputStreamReader(pushbackStream);
		} else {
			reader = new InputStreamReader(pushbackStream, encoding);
		}
	}

	public String getEncoding() {
		return reader.getEncoding();
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		return reader.read(cbuf, off, len);
	}

	public void close() throws IOException {
		reader.close();
	}

	/**
	 * Type safe enumeration class that describes the different types of Unicode
	 * BOMs.
	 */
	public static final class BOM {

		/**
		 * NONE.
		 */
		public static final BOM NONE = new BOM(new byte[]{}, "NONE");
		/**
		 * UTF-8 BOM (EF BB BF).
		 */
		public static final BOM UTF_8 = new BOM(new byte[]{(byte) 0xEF,
			(byte) 0xBB,
			(byte) 0xBF},
				"UTF-8");
		/**
		 * UTF-16, little-endian (FF FE).
		 */
		public static final BOM UTF_16_LE = new BOM(new byte[]{(byte) 0xFF,
			(byte) 0xFE},
				"UTF-16 little-endian");
		/**
		 * UTF-16, big-endian (FE FF).
		 */
		public static final BOM UTF_16_BE = new BOM(new byte[]{(byte) 0xFE,
			(byte) 0xFF},
				"UTF-16 big-endian");
		/**
		 * UTF-32, little-endian (FF FE 00 00).
		 */
		public static final BOM UTF_32_LE = new BOM(new byte[]{(byte) 0xFF,
			(byte) 0xFE,
			(byte) 0x00,
			(byte) 0x00},
				"UTF-32 little-endian");
		/**
		 * UTF-32, big-endian (00 00 FE FF).
		 */
		public static final BOM UTF_32_BE = new BOM(new byte[]{(byte) 0x00,
			(byte) 0x00,
			(byte) 0xFE,
			(byte) 0xFF},
				"UTF-32 big-endian");

		/**
		 * Returns a <code>String</code> representation of this <code>BOM</code>
		 * value.
		 */
		public final String toString() {
			return description;
		}

		/**
		 * Returns the bytes corresponding to this <code>BOM</code> value.
		 */
		public final byte[] getBytes() {
			final int length = bytes.length;
			final byte[] result = new byte[length];

			// Make a defensive copy
			System.arraycopy(bytes, 0, result, 0, length);

			return result;
		}

		private BOM(final byte bom[], final String description) {
			assert (bom != null) : "invalid BOM: null is not allowed";
			assert (description != null) : "invalid description: null is not allowed";
			assert (description.length() != 0) : "invalid description: empty string is not allowed";

			this.bytes = bom;
			this.description = description;
		}
		final byte bytes[];
		private final String description;
	} // BOM
}
