package city.smartb.fs.s2.file.app

import city.smartb.hpp.trace.sourcing.ssm.app.config.SpringTestBase
import org.springframework.beans.factory.annotation.Autowired

internal class FileDeciderSourcingTest: SpringTestBase() {

	@Autowired
	lateinit var fundReceiptDeciderSourcingImpl: FileDeciderSourcingImpl

}
