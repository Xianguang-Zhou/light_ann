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
package org.zxg.ai.lnn.tensor;

import org.zxg.ai.lnn.LnnException;

/**
 * @author <a href="mailto:xianguang.zhou@outlook.com">Xianguang Zhou</a>
 */
public class ShapeException extends LnnException {

	private static final long serialVersionUID = 1L;

	public ShapeException() {
	}

	public ShapeException(String message) {
		super(message);
	}

	public ShapeException(Throwable cause) {
		super(cause);
	}

	public ShapeException(String message, Throwable cause) {
		super(message, cause);
	}

	public ShapeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
