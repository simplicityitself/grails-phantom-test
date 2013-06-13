package grails.plugin.phantomtest

import org.codehaus.groovy.grails.test.GrailsTestTargetPattern
import org.grails.plugins.phantomtest.PhantomGrailsTestType
import spock.lang.Specification

class PhantomGrailsTestTypeSpec extends Specification {


  def "Finds all the mocha js files in the given directory"() {
    given:
      File test = File.createTempFile("phantom", "test")
      test.delete()
      test.mkdirs()
      5.times {
        new File(test, "spec${it}.js") << "alert('nothing');"
      }

      def testype = new PhantomGrailsTestType(relativeSourcePath: test.absolutePath, name:"mocha")


    expect:
      5 == testype.prepare([] as GrailsTestTargetPattern[] , null, null)

  }
}
