#!/usr/bin/env bash
java -javaagent:dissertation/distribution/target/agent-jar-with-dependencies.jar -cp agent-test/target/agent-test-jar-with-dependencies.jar -jar dissertation/distribution/target/agent-jar-with-dependencies.jar com.dariosimonetti.dissertation.agent.test.Main
