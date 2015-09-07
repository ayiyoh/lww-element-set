package com._500px.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ HSetTest.class, LWWSetTest.class, JedisTest.class })
public class AllTests {

}
