/*
 * Copyright (c) 2019, Xianguang Zhou <xianguang.zhou@outlook.com>. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.zxg.ai.lnn.autograd;

import java.util.Arrays;

import org.zxg.ai.lnn.tensor.Tensor;

/**
 * @author <a href="mailto:xianguang.zhou@outlook.com">Xianguang Zhou</a>
 */
final class LeftDotComputation extends Computation {

	private final Tensor constant;

	public LeftDotComputation(Variable creator, Tensor constant) {
		super(creator);
		this.constant = constant;
	}

	@Override
	protected Tensor backward(Tensor forwardGradient) {
		if (0 == forwardGradient.ndim()) {
			return constant.mul(forwardGradient.scalar());
		} else if (0 == constant.ndim()) {
			return forwardGradient.mul(constant.scalar());
		} else if (0 == creator.value().ndim()) {
			return forwardGradient.mul(constant).sum();
		} else {
			forwardGradient = changeNdim(forwardGradient, creator.value().ndim(), true);
			Tensor thisGradient = changeNdim(constant, 2, false).transpose();
			return thisGradient.dot(forwardGradient);
		}
	}

	private static Tensor changeNdim(Tensor tensor, int newNdim, boolean isProductGradient) {
		int oldNdim = tensor.ndim();
		if (oldNdim < newNdim) {
			tensor = tensor.expandDims(0, newNdim - oldNdim);
		} else {
			int axisLengthProduct = 1;
			while (tensor.ndim() > newNdim) {
				int[] tensorShape = tensor.shape();
				if (isProductGradient) {
					axisLengthProduct *= tensorShape[0];
				}
				tensor = tensor.sumAxis(0);
				tensor.setShape(Arrays.copyOfRange(tensorShape, 1, tensorShape.length));
			}
			if (axisLengthProduct != 1) {
				tensor = tensor.mul(1.0f / axisLengthProduct);
			}
		}
		return tensor;
	}
}