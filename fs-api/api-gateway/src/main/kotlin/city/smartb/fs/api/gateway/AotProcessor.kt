package city.smartb.fs.api.gateway

import city.smartb.fs.api.config.FsProperties
import city.smartb.fs.api.config.JwtProperties
import city.smartb.fs.api.config.S3Properties
import city.smartb.fs.api.config.SpaceProperties
import city.smartb.fs.s2.file.domain.features.command.FileDeleteCommand
import city.smartb.fs.s2.file.domain.features.command.FileInitCommand
import city.smartb.fs.s2.file.domain.features.command.FileInitPublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FileLogCommand
import city.smartb.fs.s2.file.domain.features.command.FileRevokePublicDirectoryCommand
import city.smartb.fs.s2.file.domain.features.command.FileUploadCommand
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import io.minio.StatObjectArgs
import io.minio.Xml
import io.minio.messages.ErrorResponse
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.lang.reflect.Method
import java.util.stream.Collectors
import org.springframework.aot.generate.GenerationContext
import org.springframework.aot.hint.ExecutableMode
import org.springframework.aot.hint.RuntimeHints
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor
import org.springframework.beans.factory.aot.BeanFactoryInitializationCode
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory
import org.springframework.util.ReflectionUtils


class AotProcessor : BeanFactoryInitializationAotProcessor {

    override fun processAheadOfTime(bf: ConfigurableListableBeanFactory): BeanFactoryInitializationAotContribution {
        return  BeanFactoryInitializationAotContribution { ctx: GenerationContext, code: BeanFactoryInitializationCode? ->
            val hints = ctx.runtimeHints
            hints.resources().registerPattern("user/*")

            hints.reflection().registerType(FsProperties::class.java)
            hints.reflection().registerType(SpaceProperties::class.java)
            hints.reflection().registerType(JwtProperties::class.java)


            hints.register(FsProperties::class.java)
            hints.register(S3Properties::class.java)
            hints.register(SpaceProperties::class.java)
            hints.register(JwtProperties::class.java)


            hints.register(FileDeleteCommand::class.java)
            hints.register(FileInitCommand::class.java)
            hints.register(FileInitPublicDirectoryCommand::class.java)
            hints.register(FileLogCommand::class.java)
            hints.register(FileRevokePublicDirectoryCommand::class.java)
            hints.register(FileUploadCommand::class.java)

            hints.register(StatObjectArgs::class.java)
            hints.register(PutObjectArgs::class.java)
            hints.register(RemoveObjectArgs::class.java)


            registerXml(hints)
        }
    }

    private fun registerXml(hints: RuntimeHints) {
        hints.register(Xml::class.java)

        findAllClassesUsingClassLoader(ErrorResponse::class.java.packageName).filterNotNull().forEach {
            hints.register(it)
        }
        val methodMarshal: Method = ReflectionUtils.findMethod(
            Xml::class.java,
            "marshal",
            Object::class.java,
        )!!
        hints.reflection().registerMethod(methodMarshal, ExecutableMode.INVOKE)

        val methodUnmarshalString: Method = ReflectionUtils.findMethod(
            Xml::class.java,
            "unmarshal",
            Class::class.java,
            String::class.java
        )!!
        hints.reflection().registerMethod(methodUnmarshalString, ExecutableMode.INVOKE)

        val methodUnmarshalReader: Method = ReflectionUtils.findMethod(
            Xml::class.java,
            "unmarshal",
            Class::class.java,
            Reader::class.java
        )!!
        hints.reflection().registerMethod(methodUnmarshalReader, ExecutableMode.INVOKE)

        val validateReader: Method = ReflectionUtils.findMethod(
            Xml::class.java,
            "validate",
            Class::class.java,
            String::class.java
        )!!
        hints.reflection().registerMethod(validateReader, ExecutableMode.INVOKE)
    }

    private fun <T> RuntimeHints.register(clazz: Class<T>) {
        reflection().registerType(clazz)
        clazz.getDeclaredConstructors().forEach {
            reflection().registerConstructor(it, ExecutableMode.INVOKE)
        }
    }

    fun findAllClassesUsingClassLoader(packageName: String): Set<Class<*>?> {
        val stream = ClassLoader.getSystemClassLoader()
            .getResourceAsStream(packageName.replace("[.]".toRegex(), "/"))
        val reader = BufferedReader(InputStreamReader(stream))
        return reader.lines()
            .filter { line: String -> line.endsWith(".class") }
            .map { line: String ->
                getClass(
                    line,
                    packageName
                )
            }
            .collect(Collectors.toSet())
    }

    private fun getClass(className: String, packageName: String): Class<*>? {
        return try {
             Class.forName(
                packageName + "."
                        + className.substring(0, className.lastIndexOf('.'))
            )
        } catch (e: ClassNotFoundException) {
            null
        }
    }
}
