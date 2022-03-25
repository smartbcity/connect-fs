libs: package-kotlin

package-kotlin:
	@gradle clean build publish --stacktrace
