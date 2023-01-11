GATEWAY_NAME	   	:= smartbcity/fs-gateway
GATEWAY_IMG	    	:= ${GATEWAY_NAME}:${VERSION}
GATEWAY_PACKAGE	   	:= fs-api:api-gateway

STORYBOOK_DOCKERFILE	:= infra/docker/storybook/Dockerfile
STORYBOOK_NAME	   	 	:= smartbcity/fs-storybook
STORYBOOK_IMG	    	:= ${STORYBOOK_NAME}:${VERSION}

libs: package-kotlin
docker: package-docker
docs: package-storybook

package-kotlin:
	VERSION=${VERSION} IMAGE_NAME=${GATEWAY_NAME} ./gradlew build publish -x test
	@docker push ${GATEWAY_IMG}

package-docker:
	VERSION=${VERSION} IMAGE_NAME=${GATEWAY_NAME} ./gradlew build ${GATEWAY_PACKAGE}:bootBuildImage -x test
	@docker push ${GATEWAY_IMG}

package-storybook:
	@docker build -f ${STORYBOOK_DOCKERFILE} -t ${STORYBOOK_IMG} .
	@docker push ${STORYBOOK_IMG}
