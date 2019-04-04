/*
 * Copyright (c) 2019, Xianguang Zhou <xianguang.zhou@outlook.com>. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.zxg.ai.lnn.nn;

import org.zxg.ai.lnn.autograd.Variable;
import org.zxg.ai.lnn.opencl.Device;
import org.zxg.ai.lnn.tensor.Tensor;

/**
 * @author <a href="mailto:xianguang.zhou@outlook.com">Xianguang Zhou</a>
 */
public class Linear extends StatefulLayer {

	private int inFeatures;
	private int outFeatures;
	private Variable weight;
	private Variable bias;

	public Linear(int inFeatures, int outFeatures) {
		this(inFeatures, outFeatures, true);
	}

	public Linear(int inFeatures, int outFeatures, boolean bias) {
		this(Tensor.defaultDevice(), inFeatures, outFeatures, bias);
	}

	public Linear(Device device, int inFeatures, int outFeatures, boolean bias) {
		this(Tensor.defaultPrecision(), device, inFeatures, outFeatures, bias);
	}

	public Linear(float precision, Device device, int inFeatures, int outFeatures, boolean bias) {
		this.inFeatures = inFeatures;
		this.outFeatures = outFeatures;
		this.weight = new Variable(new Tensor(precision, device, inFeatures, outFeatures));
		registerParameter("weight", this.weight);
		if (bias) {
			this.bias = new Variable(new Tensor(precision, device, outFeatures));
			registerParameter("bias", this.bias);
		}
	}

	@Override
	public Variable[] forward(Variable... input) {
		Variable output = input[0].dot(this.weight);
		if (null != this.bias) {
			Tensor outputTensor = output.value();
			output = output
					.add(this.bias.expandDims(0, outputTensor.ndim() - 1).broadcastTo(outputTensor.shape().clone()));
		}
		return new Variable[] { output };
	}

	@Override
	public Tensor[] forward(Tensor... input) {
		Tensor output = input[0].dot(this.weight.value());
		if (null != this.bias) {
			output = output.add(this.bias.value().expandDims(0, output.ndim() - 1).broadcastTo(output.shape().clone()));
		}
		return new Tensor[] { output };
	}

	@Override
	protected void appendExtraRepresentation(StringBuilder builder) {
		builder.append("inFeatures=");
		builder.append(this.inFeatures);
		builder.append(", outFeatures=");
		builder.append(this.outFeatures);
		builder.append(", bias=");
		builder.append(null != this.bias);
	}
}
