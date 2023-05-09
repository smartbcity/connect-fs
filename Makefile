GATEWAY_NAME	   	:= smartbcity/fs-gateway
GATEWAY_IMG	    	:= ${GATEWAY_NAME}:${VERSION}
GATEWAY_PACKAGE	   	:= fs-api:api-gateway

STORYBOOK_DOCKERFILE	:= infra/docker/storybook/Dockerfile
STORYBOOK_NAME	   	 	:= smartbcity/fs-storybook
STORYBOOK_IMG	    	:= ${STORYBOOK_NAME}:${VERSION}

libs: package-kotlin
docker: docker-build docker-push
docs: docs-build docs-push

docker-build: docker-fs-api-build
docker-push: docker-fs-api-push

docs-build: package-storybook-build
docs-push: package-storybook-push


package-kotlin:
	VERSION=${VERSION} IMAGE_NAME=${GATEWAY_NAME} ./gradlew build publishToMavenLocal publish -x test

docker-fs-api-build:
	VERSION=${VERSION} IMAGE_NAME=${GATEWAY_NAME} ./gradlew ${GATEWAY_PACKAGE}:bootBuildImage -x test

docker-fs-api-push:
	@docker push ${GATEWAY_IMG}

package-storybook-build:
	@docker build -f ${STORYBOOK_DOCKERFILE} -t ${STORYBOOK_IMG} .

package-storybook-push:
	@docker push ${STORYBOOK_IMG}
