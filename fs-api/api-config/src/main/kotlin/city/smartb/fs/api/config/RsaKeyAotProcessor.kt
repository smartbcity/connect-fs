package city.smartb.fs.api.config

import java.lang.reflect.Method
import org.springframework.aot.generate.GenerationContext
import org.springframework.aot.hint.ExecutableMode
import org.springframework.aot.hint.RuntimeHints
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor
import org.springframework.beans.factory.aot.BeanFactoryInitializationCode
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.util.ReflectionUtils

class RsaKeyAotProcessor : BeanFactoryInitializationAotProcessor {

    override fun processAheadOfTime(bf: ConfigurableListableBeanFactory): BeanFactoryInitializationAotContribution {
        return  BeanFactoryInitializationAotContribution { ctx: GenerationContext, code: BeanFactoryInitializationCode? ->
            val hints = ctx.runtimeHints
            hints.resources().registerPattern("user/*")
//            hints.resources().registerPattern("user/ssm-admin.pub")

//            val method: Method = ReflectionUtils.findMethod(
//                SsmChaincodeAutoConfiguration::class.java,
//                SsmChaincodeAutoConfiguration::ssmChaincodeConfig.name,
//                SsmChaincodeProperties::class.java
//            )!!
//            hints.reflection().registerMethod(method, ExecutableMode.INVOKE)

            hints.reflection().registerType(FsProperties::class.java)
            hints.reflection().registerType(SpaceProperties::class.java)
            hints.reflection().registerType(JwtProperties::class.java)


            hints.registrer(FsProperties::class.java)
            hints.registrer(S3Properties::class.java)
            hints.registrer(SpaceProperties::class.java)
            hints.registrer(JwtProperties::class.java)
        }
    }

    private fun <T> RuntimeHints.registrer(clazz: Class<T>) {
        reflection().registerType(clazz)
        clazz.getDeclaredConstructors().forEach {
            reflection().registerConstructor(it, ExecutableMode.INVOKE)
        }
    }
}
