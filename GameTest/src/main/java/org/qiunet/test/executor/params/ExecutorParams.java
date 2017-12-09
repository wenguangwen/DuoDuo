package org.qiunet.test.executor.params;

import org.qiunet.test.executor.IExecutorInitializer;
import org.qiunet.test.response.annotation.support.ResponseScannerHandler;
import org.qiunet.test.robot.init.IRobotFactory;
import org.qiunet.test.testcase.ITestCase;
import org.qiunet.utils.classScanner.IScannerHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by qiunet.
 * 17/12/6
 */
public class ExecutorParams {
	private List<ITestCase> testCases;
	private IRobotFactory robotFactory;
	private IExecutorInitializer initializer;
	private List<IScannerHandler> scannerHandlers;

	private ExecutorParams (){}

	public List<IScannerHandler> getScannerHandlers() {
		return scannerHandlers;
	}

	public IExecutorInitializer getInitializer() {
		return initializer;
	}

	public IRobotFactory getRobotFactory() {
		return robotFactory;
	}

	public List<ITestCase> getTestCases() {
		return testCases;
	}

	public static Builder custom(){
		return new Builder();
	}

	public static class Builder {
		private Builder(){}
		private IRobotFactory robotFactory;
		private IExecutorInitializer initializer;
		private List<IScannerHandler> scannerHandlers = new ArrayList<>(3);
		private List<ITestCase> testCases = new ArrayList<>(128);

		public Builder setInitializer(IExecutorInitializer initializer) {
			this.initializer = initializer;
			return this;
		}

		public Builder addTestCase(ITestCase testCase) {
			this.testCases.add(testCase);
			return this;
		}

		public Builder addScannerHandler(IScannerHandler scannerHandler) {
			this.scannerHandlers.add(scannerHandler);
			return this;
		}
		public Builder setRobotFactory(IRobotFactory robotFactory) {
			this.robotFactory = robotFactory;
			return this;
		}

		public ExecutorParams build(){
			if (robotFactory == null) throw new IllegalArgumentException("robotFactory can not be null! ");
			if (testCases.isEmpty()) throw new IllegalArgumentException("testCases is empty! must set ITestCase more than one.");

			ExecutorParams params = new ExecutorParams();
			params.robotFactory = this.robotFactory;
			params.initializer = this.initializer;
			params.testCases = Collections.unmodifiableList(testCases);
			boolean hasResponseScanner = false;
			for (IScannerHandler scannerHandler : scannerHandlers) {
				if (hasResponseScanner = scannerHandler instanceof ResponseScannerHandler) break;
			}
			if (!hasResponseScanner) this.addScannerHandler(new ResponseScannerHandler());
			params.scannerHandlers = Collections.unmodifiableList(scannerHandlers);
			return params;
		}
	}
}