WEB_DOCKERFILE	:= infra/docker/example-platform-web/Dockerfile
WEB_NAME	    := smartbcity/example-platform-web
WEB_IMG	        := ${WEB_NAME}:${VERSION}
WEB_LATEST		:= ${WEB_NAME}:latest

GATEWAY_NAME	   	:= smartbcity/example-platform-gateway
GATEWAY_IMG	    	:= ${GATEWAY_NAME}:${VERSION}
GATEWAY_LATEST		:= ${GATEWAY_NAME}:latest
GATEWAY_PACKAGE	   	:= platform:api:api-gateway

package: package-gateway package-web package-docs

package-gateway:
	VERSION=${VERSION} IMAGE_NAME=${GATEWAY_NAME} ./gradlew build ${GATEWAY_PACKAGE}:bootBuildImage -x test

package-web:
	@docker build --no-cache=true -f ${WEB_DOCKERFILE} -t ${WEB_IMG} .

package-docs:
	@docker build --no-cache=true -f ${WEB_DOCKERFILE} -t ${WEB_IMG} .
