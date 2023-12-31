package org.yipuran.json.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * AllTests
 */
@RunWith(Suite.class)
@SuiteClasses({
	JsonNodeParseTest.class,
	EpocTest.class,
})
public class AllTests{

}
