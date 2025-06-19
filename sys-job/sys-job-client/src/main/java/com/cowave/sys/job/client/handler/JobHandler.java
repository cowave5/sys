package com.cowave.sys.job.client.handler;

/**
 * @author xuxueli/shanhuiming
 */
public abstract class JobHandler {

	public abstract void execute() throws Exception;

	public void init() throws Exception {

	}

	public void destroy() throws Exception {

	}
}
