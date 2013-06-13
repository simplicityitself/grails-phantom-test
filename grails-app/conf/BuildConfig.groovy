grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
  inherits("global") {
  }

  log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

  repositories {
    grailsCentral()
    mavenCentral()
  }
  dependencies {
    test "org.spockframework:spock-grails-support:0.7-groovy-2.0"
  }
  plugins {
    test(":spock:0.7") {
      exclude "spock-grails-support"
    }
  }
}
