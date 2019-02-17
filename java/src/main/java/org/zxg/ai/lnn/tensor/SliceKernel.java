/*
 * Copyright (c) 2019, Xianguang Zhou <xianguang.zhou@outlook.com>. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.zxg.ai.lnn.tensor;

import com.aparapi.Kernel;

/**
 * @author <a href="mailto:xianguang.zhou@outlook.com">Xianguang Zhou</a>
 */
class SliceKernel extends Kernel {

	@Constant
	int[] begin_$constant$, sourceDimSizes_$constant$, resultDimSizes_$constant$, ndim_$constant$;
	float[] source, result;

	SliceKernel(int[] begin, Tensor source, Tensor result) {
		this.begin_$constant$ = begin;
		this.source = source.data;
		this.result = result.data;
		this.sourceDimSizes_$constant$ = source.dimSizes;
		this.resultDimSizes_$constant$ = result.dimSizes;
		this.ndim_$constant$ = new int[] { result.dimSizes.length };
	}

	void execute() {
		execute(result.length);
		dispose();
	}

	@Override
	public void run() {
		final int gid = getGlobalId();
		final int ndim = ndim_$constant$[0];
		int sourceIndex = 0;
		for (int resultIndex = gid, dimSizesIndex = 0; dimSizesIndex < ndim; dimSizesIndex++) {
			sourceIndex += (((resultIndex / resultDimSizes_$constant$[dimSizesIndex]) + begin_$constant$[dimSizesIndex])
					* sourceDimSizes_$constant$[dimSizesIndex]);
			resultIndex %= resultDimSizes_$constant$[dimSizesIndex];
		}
		result[gid] = source[sourceIndex];
	}
}