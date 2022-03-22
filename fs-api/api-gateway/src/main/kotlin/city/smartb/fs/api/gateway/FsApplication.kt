package city.smartb.fs.api.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["city.smartb.fs"])
class FsApplication

fun main(args: Array<String>) {
	runApplication<FsApplication>(*args)
}
