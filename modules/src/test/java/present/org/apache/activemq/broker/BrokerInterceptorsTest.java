/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.activemq.broker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jboss.eap.additional.testsuite.annotations.EapAdditionalTestsuite;

@EapAdditionalTestsuite({"modules/testcases/jdkAll/master/activemq/src/main/java#5.15.0"})
public class BrokerInterceptorsTest {

    private BrokerService brokerService;

    @Before
    public void setUp() throws Exception {
        brokerService = new BrokerService();
        brokerService.setAdvisorySupport(true);
        brokerService.setPersistent(false);
        brokerService.setUseJmx(false);
        brokerService.start();
    }

    @After
    public void tearDown() throws Exception {
        if (brokerService != null) {
            brokerService.stop();
        }
    }

    @Test
    public void testNavigateInterceptors() throws Exception {
        Broker b = brokerService.getBroker();
        Assert.assertTrue(b instanceof BrokerFilter);
        
        BrokerFilter bf = (BrokerFilter) b;
        int count = 0;
        while (bf != null) {
            Broker next = bf.getNext();
            bf = next instanceof BrokerFilter ? (BrokerFilter) next : null;
            count++;
        }
        // a few Broker interceptors are created because of the config (i.e. AdvisoryBroker)
        Assert.assertTrue(count > 1);
    }

}
