/*
 * Copyright (c) 2019, Xianguang Zhou <xianguang.zhou@outlook.com>. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.zxg.ai.lnn.opencl;

import com.jogamp.opencl.CLCommandQueue;
import com.jogamp.opencl.CLKernel;

/**
 * @author <a href="mailto:xianguang.zhou@outlook.com">Xianguang Zhou</a>
 */
public class FloatArg extends Arg {

	private float number;

	public FloatArg(float number) {
		this.number = number;
	}

	@Override
	public void input(CLKernel kernel, CLCommandQueue queue) {
		kernel.putArg(number);
	}
}