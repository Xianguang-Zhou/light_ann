/*
 * Copyright (c) 2019, Xianguang Zhou <xianguang.zhou@outlook.com>. All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.zxg.ai.lnn.opencl;

import java.nio.Buffer;
import java.nio.FloatBuffer;

/**
 * @author <a href="mailto:xianguang.zhou@outlook.com">Xianguang Zhou</a>
 */
public class FloatArray extends BufferArray {

	private final FloatBuffer buffer;
	public final int length;

	public static void copy(FloatArray src, int srcPos, FloatArray dest, int destPos, int length) {
		final FloatBuffer srcBuffer = src.buffer;
		final FloatBuffer destBuffer = dest.buffer;
		final int originalSrcPosition = srcBuffer.position();
		final int originalSrcLimit = srcBuffer.limit();
		final int originalDestPosition = destBuffer.position();
		try {
			srcBuffer.position(srcPos);
			srcBuffer.limit(srcPos + length);
			destBuffer.position(destPos);
			destBuffer.put(srcBuffer);
		} finally {
			srcBuffer.position(originalSrcPosition);
			srcBuffer.limit(originalSrcLimit);
			destBuffer.position(originalDestPosition);
		}
	}

	public static FloatArray copyOfRange(FloatArray original, int from, int to) {
		int newLength = to - from;
		if (newLength < 0) {
			throw new IllegalArgumentException(from + " > " + to);
		}
		FloatArray newArray = new FloatArray(newLength);
		copy(original, from, newArray, 0, Math.min(original.length - from, newLength));
		return newArray;
	}

	public FloatArray(int length) {
		this.buffer = Buffers.newDirectFloatBuffer(length);
		this.length = length;
	}

	public FloatArray(float[] elements) {
		this.buffer = Buffers.newDirectFloatBuffer(elements);
		this.length = elements.length;
	}

	public FloatArray(FloatArray other) {
		this.buffer = Buffers.copyFloatBuffer(other.buffer);
		this.length = other.length;
	}

	public FloatArray(FloatBuffer buffer) {
		this.buffer = Buffers.copyFloatBuffer(buffer);
		this.length = this.buffer.capacity() / Buffers.SIZEOF_FLOAT;
	}

	public float get(int index) {
		return buffer.get(index);
	}

	public void set(int index, float value) {
		buffer.put(index, value);
	}

	public void get(int begin, float[] elements, int offset, int length) {
		final int originalPosition = buffer.position();
		try {
			buffer.position(begin);
			buffer.get(elements, offset, length);
		} finally {
			buffer.position(originalPosition);
		}
	}

	public float[] get(int begin, int end) {
		float[] elements = new float[end - begin];
		get(begin, elements, 0, elements.length);
		return elements;
	}

	public float[] get() {
		return get(0, length);
	}

	public void set(int begin, float[] elements, int offset, int length) {
		final int originalPosition = buffer.position();
		try {
			buffer.position(begin);
			buffer.put(elements, offset, length);
		} finally {
			buffer.position(originalPosition);
		}
	}

	public void set(int begin, float[] elements, int offset) {
		set(begin, elements, offset, elements.length - offset);
	}

	public void set(float[] elements, int offset, int length) {
		set(0, elements, offset, length);
	}

	public void set(float[] elements, int offset) {
		set(elements, offset, elements.length - offset);
	}

	public void set(int begin, float[] elements) {
		set(begin, elements, 0, elements.length);
	}

	public void set(float[] elements) {
		set(0, elements);
	}

	@Override
	public FloatArray clone() {
		return new FloatArray(this);
	}

	@Override
	public int hashCode() {
		return buffer.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FloatArray)) {
			return false;
		}
		FloatArray other = (FloatArray) obj;
		return buffer.equals(other.buffer);
	}

	@Override
	protected Buffer buffer() {
		return buffer;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append('[');
		if (length > 0) {
			int index = 0;
			for (int limit = length - 1; index < limit; index++) {
				builder.append(buffer.get(index));
				builder.append(", ");
			}
			builder.append(buffer.get(index));
		}
		builder.append(']');
		return builder.toString();
	}
}
