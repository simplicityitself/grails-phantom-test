package org.grails.plugins.phantomtest

import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.codehaus.groovy.grails.test.GrailsTestType
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher

class PhantomGrailsTestType implements GrailsTestType {

  String name

  def testFiles

  @Override
  String getRelativeSourcePath() {
    name
  }

  @Override
  int prepare(GrailsTestTargetPattern[] testTargetPatterns, File compiledClassesDir, Binding buildBinding) {
    //TODO, filtering the target patterns
    println "Checking ...."
    def tests = new File("test", relativeSourcePath).absoluteFile

    testFiles = []

    tests.eachFileRecurse {
      if (it.name.endsWith(".html")) {
        testFiles << it
      }
    }

    println "Found ${testFiles}"
    return testFiles.size()
  }

  @Override
  GrailsTestTypeResult run(GrailsTestEventPublisher eventPublisher) {

    //ensure that we output the XML reports...
    //collect the results into the grails test type result.
//    throw new IllegalStateException("Balls")
    def passed = 0
    def failed = 0

    testFiles.each { testFile ->

      eventPublisher.testCaseStart(testFile.name)
      eventPublisher.testStart("Tests")

      //TODO, somehow break up the specs info into individual tests? Maybe use a lower level prodcess control and sniff the test case output as it comes out.

      //TODO, detect the js files and have some common runner html instead, maybe a template?
      println "Executing phaont against $testFile"
      def outputFile = "target/test-reports/TEST-${testFile.name.replace('-', '').replace('.html', '.xml')}"
      def ant = new AntBuilder()
      ant.exec(outputproperty: "cmdOut", errorproperty: "cmdErr", resultproperty: "cmdExit", failonerror: "false", executable: "src/resources/phantom/phantomjs-1.9.1-linux-i686/phantomjs") {
        arg(line: "src/resources/mocha/mocha_server.js")
        arg(line: "${testFile.canonicalPath}")
      }
      if (ant.project.properties.cmdExit != "0") {
        failed++
        eventPublisher.testFailure("Tests", ant.project.properties.cmdErr, false)
        eventPublisher.testCaseEnd(testFile.name, ant.project.properties.cmdOut, ant.project.properties.cmdErr)
      } else {
        passed++
        eventPublisher.testEnd("Tests")
        eventPublisher.testCaseEnd(testFile.name)
      }
      new File(outputFile).write(ant.project.properties.cmdOut)
    }

    return [
        getPassCount: { passed },
        getFailCount: { failed }
    ] as GrailsTestTypeResult
  }

  @Override
  void cleanup() {
//    println "CLEANUP JS tests"
//    throw new IllegalStateException("Balls")

    //TODO, destroy phantom instances, unless we can destroy them above, which may well be preferred.
  }
}
