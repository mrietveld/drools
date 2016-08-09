/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kie.test.util.test;

import java.lang.reflect.Method;

import org.junit.Assume;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class BrokenTestWatcherRule implements TestRule {

    private static final Logger logger = LoggerFactory.getLogger(BrokenTestWatcherRule.class);
    private final boolean brokenTestParameter;

    /**
     *
     * @param brokenParameter The parameter passed to the test run.
     */
    public BrokenTestWatcherRule(boolean brokenParameter) {
        this.brokenTestParameter = brokenParameter;
    }

    /* (non-Javadoc)
     * @see org.junit.rules.TestRule#apply(org.junit.runners.model.Statement, org.junit.runner.Description)
     */
    @Override
    public Statement apply(Statement base, Description description) {
        final boolean [] testBroken = { false };
        try {
            String methodName = description.getMethodName();
            int i = methodName.indexOf("[");
            if (i > 0) {
                String type = methodName.substring(i);
                type = type.substring(1, type.indexOf(']'));
                logger.info( "> " + type );

                methodName = methodName.substring(0, i);
            }
            Method method = description.getTestClass().getMethod(methodName);
            testBroken[0] = (method.getAnnotation(Broken.class) != null);
        } catch (Exception ex) {
            // ignore
        }

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {
                if( brokenTestParameter ) {
                    Assume.assumeTrue( testBroken[0] );
                } else {
                    Assume.assumeFalse( testBroken[0] );
                }
                base.evaluate();
            }
        };

    }


}