package org.grails.plugins.phantomtest

import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.codehaus.groovy.grails.test.GrailsTestType
import org.codehaus.groovy.grails.test.GrailsTestTypeResult
import org.codehaus.groovy.grails.test.event.GrailsTestEventPublisher

class PhantomGrailsTestType implements GrailsTestType {

  String relativeSourcePath
  String name

  PhantomGrailsTestType(String name, String sourceDirectory) {
    this.name = name
    this.relativeSourcePath = sourceDirectory
  }

  @Override
  int prepare(GrailsTestTargetPattern[] testTargetPatterns, File compiledClassesDir, Binding buildBinding) {
    // generate an HTML suite... ?

    //todo, detect the test suites to run. select the server js to use to power phantom.

    return 20
  }

  @Override
  GrailsTestTypeResult run(GrailsTestEventPublisher eventPublisher) {
//    println "RUNNING ...... "
    //todo, execute phantom for each test suite we detected above.
    //ensure that we output the XML reports...
    //collect the results into the grails test type result.
//    throw new IllegalStateException("Balls")


    //TODO.
    // status events for the IDE
    // XML for junit reports/ TC.
    // number output for the final counts


    //This gives the interaction with the IDE, via the status events
    eventPublisher.testCaseStart("WibbleMonkey")
    eventPublisher.testStart("Arse")
    eventPublisher.testFailure("Arse", "Failure of reason", true)

    eventPublisher.testStart("Arse")
    eventPublisher.testFailure("Arse", "Failure of reason", false)

    eventPublisher.testStart("Arse")
    eventPublisher.testFailure("Arse", "Failure of reason", true)

    eventPublisher.testStart("Arse")
    eventPublisher.testFailure("Arse", "Failure of reason", false)

    eventPublisher.testStart("Arse")
    eventPublisher.testFailure("Arse", "Failure of reason", true)

    eventPublisher.testCaseEnd("WibbleMonkey")
    eventPublisher.testCaseStart("Bum Fluff")

    eventPublisher.testStart("Umph")
    eventPublisher.testEnd("Umph")

    eventPublisher.testCaseEnd("Bum Fluff")
    return [
        getPassCount: { 4 },
        getFailCount: { 16}
    ] as GrailsTestTypeResult
  }

  @Override
  void cleanup() {
//    println "CLEANUP JS tests"
//    throw new IllegalStateException("Balls")

    //TODO, destroy phantom instances, unless we can destroy them above, which may well be preferred.
  }
}
