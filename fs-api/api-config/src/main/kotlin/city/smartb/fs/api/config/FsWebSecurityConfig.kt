package city.smartb.fs.api.config

import city.smartb.i2.spring.boot.auth.config.WebSecurityConfig
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Configuration

@Configuration
class FsWebSecurityConfig(
    override val applicationContext: ApplicationContext
): WebSecurityConfig {
    override val contextPath: String = ""
}
