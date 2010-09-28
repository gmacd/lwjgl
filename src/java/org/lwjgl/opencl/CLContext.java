/*
 * Copyright (c) 2002-2010 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lwjgl.opencl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is a wrapper around a cl_context pointer.
 *
 * @author Spasi
 */
public final class CLContext extends CLObjectChild<CLPlatform> {

	private static final CLContextUtil util = (CLContextUtil)CLPlatform.getInfoUtilInstance(CLContext.class, "CL_CONTEXT_UTIL");

	private final CLObjectRegistry<CLCommandQueue> clCommandQueues;
	private final CLObjectRegistry<CLMem> clMems;
	private final CLObjectRegistry<CLSampler> clSamplers;
	private final CLObjectRegistry<CLProgram> clPrograms;
	private final CLObjectRegistry<CLEvent> clEvents;

	/** Global registry for build callbacks. */
	static final Map<Long, CLProgram> clProgramsGlobal = new HashMap<Long, CLProgram>();

	/** Global registry for event callbacks. */
	static final Map<Long, CLEvent> clEventsGlobal = new HashMap<Long, CLEvent>();

	CLContext(final long pointer, final CLPlatform platform) {
		super(pointer, platform);

		// We do not need to register the context with the platform,
		// there is no API that returns cl_context, except clCreateContext.

		if ( isValid() ) {
			clCommandQueues = new CLObjectRegistry<CLCommandQueue>();
			clMems = new CLObjectRegistry<CLMem>();
			clSamplers = new CLObjectRegistry<CLSampler>();
			clPrograms = new CLObjectRegistryGlobal<CLProgram>(clProgramsGlobal);
			clEvents = new CLObjectRegistryGlobal<CLEvent>(clEventsGlobal);
		} else {
			clCommandQueues = null;
			clMems = null;
			clSamplers = null;
			clPrograms = null;
			clEvents = null;
		}
	}

	/**
	 * Returns a CLCommandQueue associated with this context.
	 *
	 * @param id the command queue object id
	 *
	 * @return the CLCommandQueue object
	 */
	public CLCommandQueue getCLCommandQueue(final long id) { return clCommandQueues.getObject(id); }

	/**
	 * Returns a CLMem associated with this context.
	 *
	 * @param id the memory object id
	 *
	 * @return the CLMem object
	 */
	public CLMem getCLMem(final long id) { return clMems.getObject(id); }

	/**
	 * Returns a CLSampler associated with this context.
	 *
	 * @param id the sampler object id
	 *
	 * @return the CLSampler object
	 */
	public CLSampler getCLSampler(final long id) { return clSamplers.getObject(id); }

	/**
	 * Returns a CLProgram associated with this context.
	 *
	 * @param id the program object id
	 *
	 * @return the CLProgram object
	 */
	public CLProgram getCLProgram(final long id) { return clPrograms.getObject(id); }

	/**
	 * Returns a user CLEvent associated with this context.
	 *
	 * @param id the event object id
	 *
	 * @return the CLEvent object
	 */
	public CLEvent getCLEvent(final long id) { return clEvents.getObject(id); }

	// ---------------[ UTILITY METHODS ]---------------

	/**
	 * Returns the integer value of the specified parameter.
	 *
	 * @param param_name the parameter
	 *
	 * @return the parameter value
	 */
	public int getInfoInt(int param_name) {
		return util.getInfoInt(this, param_name);
	}

	/**
	 * Returns the list of devices in context.
	 *
	 * @return the list of devices
	 */
	public List<CLDevice> getInfoDevices() {
		return util.getInfoDevices(this);
	}

	/** CLContext utility methods interface. */
	interface CLContextUtil extends InfoUtil<CLContext> {

		List<CLDevice> getInfoDevices(CLContext context);

	}

	// -------[ IMPLEMENTATION STUFF BELOW ]-------

	CLObjectRegistry<CLCommandQueue> getCLCommandQueueRegistry() { return clCommandQueues; }

	CLObjectRegistry<CLMem> getCLMemRegistry() { return clMems; }

	CLObjectRegistry<CLSampler> getCLSamplerRegistry() { return clSamplers; }

	CLObjectRegistry<CLProgram> getCLProgramRegistry() { return clPrograms; }

	CLObjectRegistry<CLEvent> getCLEventRegistry() { return clEvents; }

	static CLProgram getCLProgramGlobal(final long id) { return clProgramsGlobal.get(id); }

	static CLEvent getCLEventGlobal(final long id) { return clEventsGlobal.get(id); }

}