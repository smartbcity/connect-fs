GATEWAY_NAME	   	:= smartbcity/fs-gateway
GATEWAY_IMG	    	:= ${GATEWAY_NAME}:${VERSION}
GATEWAY_PACKAGE	   	:= fs-api:api-gateway

package: package-kotlin

package-kotlin:
	VERSION=${VERSION} IMAGE_NAME=${GATEWAY_NAME} ./gradlew clean build ${GATEWAY_PACKAGE}:bootBuildImage publish -x test
	@docker push ${GATEWAY_IMG}
