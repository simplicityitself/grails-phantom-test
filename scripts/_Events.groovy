

loadPhantomTestTypeClass = {->
  def doLoad = {-> classLoader.loadClass('org.grails.plugins.phantomtest.PhantomGrailsTestType') }
  try {
    doLoad()
  } catch (ClassNotFoundException e) {
    includeTargets << grailsScript("_GrailsCompile")
    compile()
    doLoad()
  }
}

loadPhantomTestTypes = {
  def phantomTestTypeClass = loadPhantomTestTypeClass()
  binding.setVariable("phantomTests", [
      phantomTestTypeClass.newInstance("mocha", "mocha"),
      phantomTestTypeClass.newInstance("jasmine", "jasmine")
      ])
}

eventAllTestsStart = {
  event("StatusUpdate", ["LOAD TESTS!"])
  loadPhantomTestTypes()
  phasesToRun << "phantom"
}

eventPackagePluginsEnd = {
  event("StatusUpdate", ["Package plugins..."])
  loadPhantomTestTypes()
}

phantomTestPhasePreparation = { }

phantomTestPhaseCleanUp = {}


target(runJsTests: 'Runs Jasmine Tests') {
  event("StatusUpdate", ["Starting Jasmine test phase"])

  def specsToRun = []
  new File(specDir).eachFileRecurse {
    if (it.name.endsWith(".html")) {
      specsToRun << it
    }
  }

  def startTime = new Date()
  def failures = 0
  def buildPassed = true
  specsToRun.each { File spec ->
    println "Running ${spec.name}..."
    def outputFile = "${targetDir}/TEST-${spec.name.replace('-', '').replace('.html', '.xml')}"
    def ant = new AntBuilder()
    ant.exec(outputproperty: "cmdOut", errorproperty: "cmdErr", resultproperty: "cmdExit", failonerror: "false", executable: "/usr/bin/env") {
      arg(line: "DISPLAY=:1")
      arg(line: "/usr/local/bin/phantomjs")
      arg(line: "${testResourcesDir}/phantomjs-jasmine-runner.js")
      arg(line: "${spec.canonicalPath}")
    }
    if (ant.project.properties.cmdExit != "0") {
      buildPassed = false
      println "\tFAILED!"
      failures++
    } else {
      println "\tPASSED"
    }
    new File(outputFile).write(ant.project.properties.cmdOut)
  }

  println "-------------------------------------------------------"
  println "Jasmine Tests Completed in ${new Date().getTime() - startTime.getTime()}ms"
  def msg = "Jasmine Tests ${buildPassed ? "PASSED" : "FAILED"} - view reports in ${targetDir}"
  println "-------------------------------------------------------"
  event("StatusFinal", [msg])
  return buildPassed ? 0 : 1
}