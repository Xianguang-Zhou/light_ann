/*
 * Copyright (c) 2019, Xianguang Zhou <xianguang.zhou@outlook.com>. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.zxg.ai.lnn.tensor;

import com.aparapi.Kernel;
import com.aparapi.Range;

/**
 * @author <a href="mailto:xianguang.zhou@outlook.com">Xianguang Zhou</a>
 */
class ProductKernel extends Kernel {

	@Constant
	int[] cacheDataShape_$constant$, resultDimSizes_$constant$, leftDimSizes_$constant$, rightDimSizes_$constant$,
			axisLimits_$constant$;
	@Constant
	float[] leftData_$constant$, rightData_$constant$;
	float[][] cacheData;
	float[] resultData;

	ProductKernel(Tensor left, Tensor right, Tensor result) {
		cacheDataShape_$constant$ = new int[] { result.data.length, right.shape[0] };
		cacheData = new float[cacheDataShape_$constant$[0]][cacheDataShape_$constant$[1]];
		resultData = result.data;
		resultDimSizes_$constant$ = result.dimSizes;
		leftData_$constant$ = left.data;
		rightData_$constant$ = right.data;
		leftDimSizes_$constant$ = left.dimSizes;
		rightDimSizes_$constant$ = right.dimSizes;
		axisLimits_$constant$ = new int[] { left.shape.length - 1, right.shape.length };
	}

	void execute() {
		execute(Range.create2D(cacheDataShape_$constant$[0], cacheDataShape_$constant$[1]), 2);
		dispose();
	}

	@Override
	public void run() {
		int passId = getPassId();
		if (passId == 0) {
			int gid0 = getGlobalId(0);
			int gid1 = getGlobalId(1);

			int resultDataIndex = gid0;
			int resultDimSizesIndex = 0;
			int resultDimSize = 0;
			int leftDataIndex = gid1;
			int dimSizesIndexLimit = axisLimits_$constant$[0];
			for (int i = 0; i < dimSizesIndexLimit;) {
				resultDimSize = resultDimSizes_$constant$[resultDimSizesIndex++];
				leftDataIndex += ((resultDataIndex / resultDimSize) * leftDimSizes_$constant$[i++]);
				resultDataIndex %= resultDimSize;
			}
			int rightDataIndex = gid1 * rightDimSizes_$constant$[0];
			dimSizesIndexLimit = axisLimits_$constant$[1];
			for (int i = 1; i < dimSizesIndexLimit;) {
				resultDimSize = resultDimSizes_$constant$[resultDimSizesIndex++];
				rightDataIndex += ((resultDataIndex / resultDimSize) * rightDimSizes_$constant$[i++]);
				resultDataIndex %= resultDimSize;
			}

			cacheData[gid0][gid1] = leftData_$constant$[leftDataIndex] * rightData_$constant$[rightDataIndex];
		} else {
			int gid1 = getGlobalId(1);
			if (gid1 == 0) {
				int gid0 = getGlobalId(0);
				float resultValue = 0;
				int cacheDataShape1 = cacheDataShape_$constant$[1];
				for (int i = 0; i < cacheDataShape1;) {
					resultValue += cacheData[gid0][i++];
				}
				resultData[gid0] = resultValue;
			}
		}
	}
}
